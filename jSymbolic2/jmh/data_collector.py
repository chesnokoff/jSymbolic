import os.path
import shutil
import sys
from multiprocessing import Pool
import tqdm

import mido

LIMITS = [i * 10000 for i in range(1, 100)]

PATH_TO_SAVE = "./"


def ticks_number(mid):
    max_sum = 0
    for i, track in enumerate(mid.tracks):
        sum = 0
        for msg in track:
            sum += msg.time
        max_sum = max(sum, max_sum)
    return max_sum


def tree(directory):
    files = []
    for dirpath, _, filenames in os.walk(directory):
        for f in filenames:
            files.append(os.path.abspath(os.path.join(dirpath, f)))
    return files


def process_mid(mid_path):
    global PATH_TO_SAVE
    try:
        mid = mido.MidiFile(mid_path)
    except Exception as e:
        # print('Could not parse {}. '.format(mid_path) + str(e), file=sys.stderr)
        return
    lenght = ticks_number(mid)
    if lenght < LIMITS[0]:
        shutil.copyfile(mid_path, PATH_TO_SAVE + "0_{}/".format(LIMITS[0]) + os.path.basename(mid_path))
        return
    if lenght >= LIMITS[-1]:
        shutil.copyfile(mid_path,
                        PATH_TO_SAVE + "{}_/".format(LIMITS[-1]) + os.path.basename(mid_path))
        return
    for i in range(1, len(LIMITS) - 1):
        if lenght >= LIMITS[i] and lenght < LIMITS[i + 1]:
            shutil.copyfile(mid_path, PATH_TO_SAVE + "{}_{}/".format(LIMITS[i], LIMITS[
                i + 1]) + os.path.basename(mid_path))
            return


def process(path: str):
    print('Start collecting files')
    files = tree(path)
    print('End collecting files')
    print('Start moving files')
    with Pool(25) as p:
        for _ in tqdm.tqdm(p.imap(process_mid, files), total=len(files)):
            pass



def prepare(save_path):
    if not os.path.isdir(save_path):
        raise ValueError("Save path must be a directory!")
    global PATH_TO_SAVE
    PATH_TO_SAVE = os.path.normpath(save_path) + "/"
    try:
        os.mkdir(PATH_TO_SAVE + "0_{}".format(LIMITS[0]))
    except Exception as e:
        pass
    try:
        os.mkdir(PATH_TO_SAVE + "{}_".format(LIMITS[-1]))
    except Exception as e:
        pass
    for i in range(1, len(LIMITS) - 1):
        try:
            os.mkdir(PATH_TO_SAVE + "{}_{}".format(LIMITS[i], LIMITS[i + 1]))
        except Exception as e:
            continue


if __name__ == "__main__":
    args = sys.argv
    if len(args) <= 2:
        print('There must be two or more given arguments.', file=sys.stderr)
        exit()
    prepare(args[-1])
    for i in range(1, len(args) - 1):
        process(args[i])
