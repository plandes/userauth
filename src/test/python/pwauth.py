#!/usr/bin/env python

import sys
import itertools as it
import fileinput

DEBUG = 0
USER_DB = {'bob': 'pass123',
           'jane': 'changeit'}

user, passwd = tuple(map(lambda x: x.strip(), it.islice(fileinput.input(), 2)))

if DEBUG:
    print(f'user: {user}, passwd: {passwd}')

status = 1
if user in USER_DB:
    if USER_DB[user] == passwd:
        status = 0
    else:
        status = 2

sys.exit(status)
