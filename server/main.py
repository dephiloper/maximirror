import os
import json
import re
from subprocess import call
from flask import Flask
from flask import request
app = Flask(__name__)

this_dirpath = os.path.dirname(os.path.realpath(__file__))
config_filepath = this_dirpath + "/../src/main/resources/config.json"
target_filepath = this_dirpath + "/../target/AssistantMirror-1.0-SNAPSHOT-jar-with-dependencies.jar"

class Config:
    def __init__(self):
        self.dict = {}
        self.status = {}

    def string(self):
        with open(config_filepath) as f:
            return f.read()

    def loadFromFile(self):
        config_string = self.string()
        data = json.loads(config_string)
        for k in data:
            self.dict[k] = data[k]
            self.status[k] = ""

    def saveToFile(self):
        with open("config_out.json", mode="w+") as f:
            f.write(json.dumps(self.dict, indent=4))
            f.close()
        return json.dumps({"data" : self.dict, "status": self.status})

    def update(self, newdict):
        def recursive(a, b, status):
            for key in a:
                if key in b:
                    if type(a[key]) == type(b[key]):
                        # TODO: also check list recursively
                        if type(a[key]) is dict:
                            status[key] = type(a[key])()
                            recursive(a[key], b[key], status)
                        else:
                            a[key] = b[key]
                            status[key] = "Ok"
                    else:
                        print("ERROR: datatype differs")
                        status[key] = "ERROR: datatype differs"
                else:
                    pass
        recursive(self.dict, newdict, self.status)

@app.route("/config")
def readconfig():
    config = Config()
    config.loadFromFile()
    return config.saveToFile()

@app.route("/updateconfig", methods=["GET"])
def writeConfig():
    config = Config()
    config.loadFromFile()
    if "config" in request.args:
        config.update(json.loads(request.args.get("config")))
    else:
        print("no config in args")
    return config.saveToFile()

@app.route("/start")
def start():
    #call(["java", "-jar", target_filepath, "&"])
    os.system("java -jar " + target_filepath + " &")
    return "starting " + target_filepath + "..."

@app.route("/stop")
def stop():
    os.system("pkill java")
    return "killing java..."

@app.route("/restart")
def restart():
    return stop() + "<br>" + start()
