# http://stackoverflow.com/questions/19859840/excluding-directories-in-os-walk
# [2015/09/03]

# 1. Check the backup.sh file beneath the Import directory
# 2. Change dir, execute the command, then come back 

import os

def toJson(fromFile, toFile):
    def convertValue(input):
        input = input.strip()
        if input.startswith("("):
            return input.replace("(", "[").replace(")","]")
        else:
            try:
                value = int(input)
                return value
            except ValueError:
                try:
                    value = float(input)
                    return value
                except ValueError:
                    return '"{}"'.format(input) 
    
    def convert(line):
        if len(line) == 0: return ""
        lines = line.split("->")
        result = r'  "{}" : {},'.format(lines[0].strip(), convertValue(lines[1]))
        return result
    
    print toFile
    with open(fromFile, "r") as f:
        lines = f.readlines()
        f.close()
        
    result = "{\n"
    for line in lines:
        result += convert(line.rstrip()) + "\n"
        
    json = result[:-2] + "\n}\n"
    
    with open(toFile, "w") as f:
        f.write(json)
        f.close()
        
def run(directory):
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith(".txt") and (file.startswith("c") or file.startswith("s")):
                fromFile = root + "/" + file
                toJson(fromFile, root + "/" + file.replace(".txt", ".json"))
                
if __name__ == "__main__":
    run(".")
    #toJson("./s2/s2.txt", "./s2/s2.json")