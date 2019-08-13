from __future__ import print_function
import math
import sys
import numpy as np
import pymysql
import pandas as pda
def geythrethods(k,station,time):
    conn=pymysql.connect(host='10.11.112.202',user="user",password='123456',db='SHUINI')
    sql="select %s from DB_JIANGYIN order by ID DESC limit 0,%s"#7200个数据那就是一小时
    i=pda.read_sql(sql%(station,time),conn)
    # print(i.describe())
    arr_data=np.array(i)
    threshod_max = np.mean(arr_data) + k * np.std(arr_data)
    threshod_min = np.mean(arr_data) - k * np.std(arr_data)
    return threshod_min,threshod_max

# print(geythrethods(1,'L0063',1))
print(geythrethods(int(sys.argv[1]),str(sys.argv[2]),int(sys.argv[3])))