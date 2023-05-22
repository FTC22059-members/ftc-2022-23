#!/usr/bin/env python

# This file returns the ranges of unicode that a given font supports

from itertools import chain
import sys

from fontTools.ttLib import TTFont
from fontTools.unicode import Unicode

with TTFont(
    sys.argv[1], 0, allowVID=0, ignoreDecompileErrors=True, fontNumber=-1
) as ttf:
    for table in ttf["cmap"].tables:
        chars = []
        for item in table.cmap.items():
            chars.append(item[0])

        chars.sort()

        startChar = 0
        ranges = []
        for index in range(len(chars) - 1):
            if (chars[index] + 1) == chars[index + 1]:
                endChar = index

            else:
                endChar = index
                ranges.append("new int[]{" + str(chars[startChar]) + ", " + str(chars[endChar]) + "}")
                startChar = index + 1
        print("{" + ", ".join(ranges) + "}")
