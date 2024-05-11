import os
import subprocess
import sys


def tree(directory):
    files = []
    for dirpath, _, filenames in os.walk(directory):
        for f in filenames:
            files.append(os.path.relpath(os.path.join(dirpath, f)))
    return files


if __name__ == "__main__":
    args = sys.argv
    if len(args) != 2:
        print('There must be one given arguments.', file=sys.stderr)
        exit()
    configs = tree(args[1])
    command = ["java", "-jar", "build/libs/jsymbolic2-benchmark-1.0-SNAPSHOT.jar", "-c",
               ';'.join(configs)]
    subprocess.run(command)
