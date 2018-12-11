#!/usr/bin/env python3
import logging
import os
import shutil
import requests
import sys
import time
import subprocess

logging.basicConfig(format="%(asctime)s [%(levelname)s] %(message)s", datefmt="%Y-%m-%d %H:%M:%S", level=logging.DEBUG)

home_dir = os.path.expanduser("~/")
temp_dir = home_dir + "mirror_temp"
deploy_dir = home_dir + "mirror_deploy"
backup_dir = home_dir + "mirror_backup"

service_name = "mirror"

# .zip, .bz, bz2, .tar
accepted_archive_content_types = ["application/zip", "application/x-bzip", "application/x-bzip2", "application/x-tar"]


def is_archive_content_type_accepted(content_type: str) -> bool:
    lower_content_type = content_type.lower()
    return any(
        lower_content_type.startswith(allowed_content_type) for allowed_content_type in accepted_archive_content_types)


def backup_old_release(clean_dir_after: bool = True):
    try:
        with open(os.path.join(deploy_dir, "release")) as release_file:
            release_file_content = "".join(release_file.readlines())
    except IOError:
        logging.error("the currently deployed version does not contain a release file! continuing with %s",
                      "VERSION_FILE_NOT_FOUND")
        release_file_content = "VERSION_FILE_NOT_FOUND"

    file_name = str.format("backup_%s_%s" % (release_file_content, time.strftime("%Y-%m-%d_%H:%M:%S")))
    file = os.path.join(backup_dir, file_name)
    shutil.make_archive(file, "zip", deploy_dir)
    if clean_dir_after:
        shutil.rmtree(deploy_dir)
        os.makedirs(deploy_dir)


logging.info("starting update process")

headers = {"Accept": "application/vnd.github.v3+json"}
api_url = "https://api.github.com/repos/dephiloper/maximirror/releases/latest"
response = requests.get(api_url, headers=headers)

if not 200 <= response.status_code < 300:
    logging.error("received status code %d from %s! aborting mission!", response.status_code, api_url)
    sys.exit()

if not response.headers["content-type"].lower().startswith("application/json"):
    logging.error("received content-type %s from %s! aborting mission!", response.headers["content-type"], api_url)
    sys.exit()

release = response.json()
assets = release["assets"]
if assets is None or len(assets) == 0:
    logging.info("no release with a asset found! aborting mission!")
    sys.exit()

archive_assets = list(
    filter(lambda asset: "content_type" in asset and is_archive_content_type_accepted(asset["content_type"]), assets))
if archive_assets is None or len(archive_assets) == 0:
    logging.info("no archive asset found! aborting mission!")
    sys.exit()

if not os.path.exists(temp_dir):
    logging.info("the configured temp_dir (%s) doesn't exist. creating it now!", temp_dir)
    os.makedirs(temp_dir)

if not os.path.exists(deploy_dir):
    logging.info("the configured deploy_dir (%s) doesn't exist. creating it now!", deploy_dir)
    os.makedirs(deploy_dir)

if not os.path.exists(backup_dir):
    logging.info("the configured backup_dir (%s) doesn't exist. creating it now!", backup_dir)
    os.makedirs(backup_dir)

logging.info("stopping service")
subprocess.call("systemctl stop {}".format(service_name).split())

shutil.rmtree(temp_dir)
os.makedirs(temp_dir)

logging.info("backing up old release")
backup_old_release()
for asset in archive_assets:
    logging.info("downloading %s from %s", asset["name"], asset["browser_download_url"])
    subprocess.call("wget {} -P {}".format(asset["browser_download_url"], temp_dir).split())
    shutil.unpack_archive(os.path.join(temp_dir, asset["name"]), deploy_dir)

for root, dirs, files in os.walk(deploy_dir):
    for name in files:
        if name.endswith(".py") or name.endswith(".sh"):
            subprocess.call("chmod u+x {}".format(os.path.join(root, name)).split())

logging.info("start service")
subprocess.call("systemctl start {}".format(service_name).split())

logging.info("update done")
