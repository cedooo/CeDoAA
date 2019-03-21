#!/bin/sh
# -*- coding: utf-8 -*-
import time

import redis
from flask import Flask
from flask import request

app = Flask(__name__)
cache = redis.Redis(host='redis', port=6379)


def get_hit_count():
    retries = 5
    while True:
        try:
            return cache.incr('hits')
        except redis.exceptions.ConnectionError as exc:
            if retries == 0:
                raise exc
            retries -= 1
            time.sleep(0.5)


@app.route('/')
def hello():
    count = get_hit_count()
    return 'Hello World! I have been seen {} times.\n'.format(count)

@app.route('/cedoci', methods=['POST'])
def cedoci():
    """处理git webhook"""
    return 'the git push!\n'

@app.route('/env', methods=['PUT'])
def env():
    """更新环境数据"""
    return 'env!\n'

@app.route('/bk', methods=['GET'])
def bk():
    """获取数据备份"""
    return 'bk!\n'

@app.route('/msgs', methods=['GET'])
def msgs():
    """获取未读消息"""
    return 'msgs!\n'
	
if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True)