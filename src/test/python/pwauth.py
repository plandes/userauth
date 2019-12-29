#!/usr/bin/env python3

import sys
import itertools as it
import fileinput

DEBUG = len(sys.argv) > 2 and sys.argv[2] == '--debug'
USER_DB = {'bob': 'pass123',
           'jane': 'changeit'}

user, passwd = tuple(map(lambda x: x.strip(), it.islice(fileinput.input(), 2)))

if DEBUG:
    print('user: %(user)s, passwd: %(passwd)s' %
          {'user': user, 'passwd': passwd})

status = 1
if user in USER_DB:
    if DEBUG:
        print('db: found')
    if USER_DB[user] == passwd:
        status = 0
    else:
        status = 2

if DEBUG:
    print('status:', status)

sys.exit(status)
