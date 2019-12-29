#!/usr/bin/env python3

import sys


PASS_DB = {'bob': 'bob:*:701:701:Bob Copymeister:/dev/null:/usr/sbin/nologin',
           'jane': 'jane:*:702:702:Jane Smith:/dev/null:/usr/sbin/nologin'}

DBS = {'passwd': PASS_DB}

args = sys.argv

db = DBS[args[1]]

if len(args) > 2:
    print(db[args[2]])
else:
    for line in db.values():
        print(line)
