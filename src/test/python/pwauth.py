#!/usr/bin/env python

import sys
import itertools as it
import fileinput

DEBUG = 0
USERS = {'bob': 'pass123',
         'jane': 'changeit'}

user, passwd = tuple(map(lambda x: x.strip(), it.islice(fileinput.input(), 2)))

if DEBUG:
    print(f'user: {user}, passwd: {passwd}')

status = 1
if user in USERS:
    if USERS['user'] == passwd:
        status = 0
    else:
        status = 2

sys.exit(status)
