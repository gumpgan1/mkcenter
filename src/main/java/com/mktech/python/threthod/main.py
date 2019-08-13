import pandas as pda
import numpy as np
import seaborn as sns
import matplotlib as plt
from matplotlib import pyplot as plt
import scipy.stats as ss
from sklearn.preprocessing import MinMaxScaler,StandardScaler
from sklearn.preprocessing import LabelEncoder,OneHotEncoder,Normalizer
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis
from  sklearn.decomposition import PCA
import os
os.environ['PATH']+=os.pathsep+"D:/software/graphviz-release/bin"
import pydotplus
f=open('D:/其他测试文件/HR_comma_sep.csv','rb')
df=pda.read_csv(f)

# plt.title("salary")
# sns.set_style(style='ticks')
# sns.set_context(context='poster',font_scale=0.8)
#绘制柱状图
# sns.set_palette('red')
# sns.countplot(x='salary',data=df)
# plt.xlabel("salary")
# plt.ylabel("number")
#
# plt.bar(np.arange(len(df['salary'].value_counts()))+0.5,df['salary'].value_counts(),width=0.5)
# for x,y in zip(np.arange(len(df['salary'].value_counts()))+0.5,df['salary'].value_counts()):
#     plt.text(x,y,y,ha="center",va="bottom")
# plt.show()


#下面绘制直方图
# f=plt.figure()
# f.add_subplot(1,3,1)
# sns.distplot(df['satisfaction_level'],bins=10)
# sns.set_palette("RdBu", n_colors=5)
# f.add_subplot(1,3,2)
# sns.distplot(df['average_montly_hours'],bins=10)
# sns.set_palette("RdBu", n_colors=7)
# f.add_subplot(1,3,3)
# sns.distplot(df['time_spend_company'],bins=10)
# plt.show()


#绘制箱线图
# sns.boxplot(y=df['satisfaction_level'])
# sns.boxplot(x=df['time_spend_company'],saturation=0.75,whis0=3)
# plt.show()

#绘制这线图（用以反映随着时间的演进，离职率的变化情况）
# sub_df=df.groupby('time_spend_company').mean()
# sns.pointplot(sub_df.inedx,sub_df['left'])

# sns.pointplot(y=df['average_montly_hours'],x=df['sales'],data=df)
# plt.show()

#绘制饼图
# lbs=df['sales'].value_counts().index
# explodes=[0.5 if i=='sales' else 0 for i in lbs]#这一步可以使得sales这个部门单独出来，使得这个部门在饼图中飞出
# plt.pie(df['sales'].value_counts(normalize=True),explode=explodes,labels=lbs)
# plt.show()

#交叉分析
#1、热力图
# dp_indices=df.groupby(by='sales').indices
# # sales_values=df['left'].iloc[dp_indices['sales']].values
# # technical_values=df['left'].iloc[dp_indices['technical']].values
# dp_keys=list(dp_indices.keys())
# dp_t_mat=np.zeros([len(dp_keys),len(dp_keys)])
# for i in range(len(dp_keys)):
#     for j in range(len(dp_keys)):
#         p_values=ss.ttest_ind(df['left'].iloc[dp_indices[dp_keys[i]]].values,
#         df['left'].iloc[dp_indices[dp_keys[j]]].values)[1]
#         dp_t_mat[i][j]=p_values
# sns.heatmap(dp_t_mat,xticklabels=dp_keys,yticklabels=dp_keys)
# plt.show()
#2、透视表+热力图
# piv_tb=pda.pivot(df,values='left',rowsl=['promotion_last_5years','salary'],columns=['Work_accident'],aggfunc=np.mean())#总是提示出错
# sns.heatmap(piv_tb,vmin=0,vmax=1,cmap=sns.color_palette('Reds',n_colors=246))
# plt.show()

#分组分析
# sns.barplot(x='salary',y='left',hue='sales',data=df)
# plt.show()

# sl_s=df['satisfaction_level']
# sns.barplot(list(range(len(sl_s))),sl_s.sort_values())
# plt.show()

#相关分析
#用热力图显示连续属性的相关性
# sns.heatmap(df.corr(),vmin=-1,vmax=1,cmap=sns.color_palette('RdBu',n_colors=128))
# plt.show()

#离散属性相关性的分析
# s1=pda.Series(['X1','X1','X2','X2','X2','X2'])
# s2=pda.Series(['Y1','Y1','Y1','Y2','Y2','Y2'])
# def getEntropy(s):
#     prt_ary=pda.groupby(s,by=s).count().values/float(len(s))
#     return -((np.log2(prt_ary))*prt_ary).sum()
# print('熵为:',getEntropy(s1))

# from sklearn.decomposition import PCA
# my_pca=PCA(n_components=7)
# low_mat=my_pca.fit_transform(df.drop(labels=['salary','sales','left'],axis=1))
# sns.heatmap(pda.DataFrame(low_mat).corr(),vmin=-1,vmax=1,cmap=sns.color_palette('RdBu',n_colors=128))
# plt.show()



#下面是整个数据处理的大总结！！！！！！！！！干货满满哦
#s1：satisfaction_level——False:MinMaxScaler;True:StandardScaler
#le:last_evaluation——False:MinMaxScaler;True:StandardScaler
#np:number_project——False:MinMaxScaler;True:StandardScaler
#amh:average_montly_hours——False:MinMaxScaler;True:StandardScaler
#tsc:time_spend_company——False:MinMaxScaler;True:StandardScaler
#wa:Work_accident——False:MinMaxScaler;True:StandardScaler
#pl5:promotion_last_5years——False:MinMaxScaler;True:StandardScaler
#dp:department——False:LabelEncoding;True:OneHotEncoding
#slr:salary——False:LabelEncoding;True:OneHotEncoding
def hr_processing(sl=True,le=True,np=True,amh=True,tsc=True,wa=True,pl5=True,dp=True,slr=True,low_d=True,ldn=1):#low_d是是否降维的参数，ldn是想要降维到的维度
    f = open('D:/其他测试文件/HR_comma_sep.csv', 'rb')
    df = pda.read_csv(f)
    #1、得到标注
    lables=df['left']
    df['left'].dropna(axis=0)
    #2、清洗数据
    df=df.dropna(subset=['satisfaction_level'])
    df=df[df['satisfaction_level']<=1]
    #3、特征选择(这里什么都不做，但是事实上，根据之前的相关系数热力图显示的，last_evaluation、number_project、average_montly_hours和left相关性都不高，因此在
    # 特征选择过程中可以不选这三个特征)
    #4、特征处理(其实就是将特征值进行归一化或标准化等操作，这里使用两种，即：MinMaxScaler;True:StandardScaler)
    scaler_lst=[sl,le,np,amh,tsc,wa,pl5]
    column_lst=['satisfaction_level','last_evaluation','number_project','average_montly_hours','time_spend_company','Work_accident','promotion_last_5years']
    for i in range(len(scaler_lst)):
        if not scaler_lst[i]:
            df[column_lst[i]]=MinMaxScaler().fit_transform(df[column_lst[i]].values.reshape(-1,1)).reshape(1,-1)[0]
        else:
            df[column_lst[i]]=StandardScaler().fit_transform(df[column_lst[i]].values.reshape(-1,1)).reshape(1,-1)[0]
    # #（2）last_evaluation的特征处理
    # for i in range(len(df['last_evaluation'])):
    #     if not [le][i]:
    #         df[df['last_evaluation'][i]]=MinMaxScaler().fit_transform(df['last_evaluation'].values.reshape(-1,1)).reshape(1,-1)[0]
    #     else:
    #         print('1111111')
    #         df[df['last_evaluation'][i]] =StandardScaler().fit_transform(df['last_evaluation'].values.reshape(-1, 1)).reshape(1, -1)[0]
    scaler_lst=[slr,dp]
    column_lst = ['salary', 'sales']
    for i in range(len(scaler_lst)):
        if not scaler_lst[i]:
            if column_lst[i]=='salary':
                df[column_lst[i]]=[map_salary(s)for s in df['salary'].values]
            else:
                df[column_lst[i]] = LabelEncoder().fit_transform(df[column_lst[i]])
            df[column_lst[i]]=MinMaxScaler().fit_transform(df[column_lst[i]].values.reshape(-1,1)).reshape(1,-1)[0]
        else:
            df=pda.get_dummies(df,columns=[column_lst[i]])
    if low_d:
        return PCA(n_components=ldn).fit_transform(df.values),lables
    return df,lables
d=dict([('low',0),('medium',1),('high',2)])
def map_salary(s):
    return d.get(s,0)
def hr_modeling(features,label):
    from sklearn.model_selection import train_test_split
    f_v=features
    f_name=pda.DataFrame(featrues).columns.values#本句代码用于画决策树；对于columns属性只有dataframe的数据才有，而将数据转化为dataframe只要pda.dataframe（）即可，
    l_v=label.values
    x_tt,x_val,y_tt,y_val=train_test_split(f_v,l_v,test_size=0.2)#x_tt是训练集，x_val是验证集,y_tt,y_val是验证集和训练的标注
    x_train,x_test,y_train,y_test=train_test_split(x_tt,y_tt,test_size=0.25)#将x_tt进一步分成训练集和测试集
    print(len(x_train),len(x_test),len(x_val))

#knn算法+朴素贝叶斯+决策树
    from sklearn.metrics import accuracy_score, recall_score, f1_score
    from sklearn.neighbors import NearestNeighbors,KNeighborsClassifier
    from sklearn.naive_bayes import GaussianNB,BernoulliNB # GaussianNB,BernoulliNB分别是高斯朴素贝叶斯以及伯努利朴素贝叶斯（如果是二值数据，如0和1，那么伯努利更好）
    from sklearn.tree import DecisionTreeClassifier,export_graphviz
    from sklearn.externals.six import StringIO
    from sklearn.svm import SVC#支持向量机
    from  sklearn.ensemble import RandomForestClassifier#随机森林
    from sklearn.ensemble import AdaBoostClassifier# AdaBoost算法
    from sklearn.linear_model import LogisticRegression#逻辑回归
    from sklearn.ensemble import GradientBoostingClassifier
    # from keras.models import Sequential
    # from keras.layers.core import Dense,Activation
    # from keras.optimizers import SGD#SGD也就是随机梯度下降算法
    # mdl=Sequential()
    # mdl.add(Dense(50,input(len(f_v[0]))))
    # mdl.add(Activation('sigmoid'))
    # mdl.add(Dense(2))
    # mdl.add(Activation('softmax'))
    # sgd=SGD(lr=0.01)
    # mdl.compile(loss='mean_squared_error',optimizer='adam')
    # mdl.fit(x_train,y_train,np.array([[0,1] if i==1 else [1,0]for i in y_train]),nb_epoch=100,batch_size=2048)
    #
    # xy_lst = [(x_train, y_train), (x_val, y_val), (x_test, y_test)]
    # for i in range(len(xy_lst)):
    #         x_part = xy_lst[i][0]
    #         y_part = xy_lst[i][1]
    #         y_pred = mdl.predict_classes(x_part)
    #         print(i)
    #         print('NN', "acc", accuracy_score(y_part, y_pred))
    #         print('NN', 'recall', recall_score(y_part, y_pred))
    #         print('NN', 'f1', f1_score(y_part, y_pred))
    # return

    models=[]
    # knn_clf=KNeighborsClassifier(n_neighbors=3)
    # models.append(("knn",KNeighborsClassifier(n_neighbors=3)))
    # models.append(('GaussianNB',GaussianNB()))
    # models.append(('DecisionTree',DecisionTreeClassifier(min_impurity_split=0.1)))
    #     # models.append(('BernoulliNB',BernoulliNB()))#基于基尼系数的决策树,其中的min_impurity_split=0.1是用于剪枝的，指切分的最小不纯度为0.1，当然也可以不设置，这样三大检验系数的数值似乎好看一点
    # models.append(('DecisionTreeEntropy',DecisionTreeClassifier(criterion='entropy')))#基于信息增益的决策树
    models.append(('SVM Classifier',SVC()))
    # models.append(('RandomFroest',RandomForestClassifier(max_features=None,bootstrap=False,n_estimators=81)))
    # models.append((' AdaBoost',AdaBoostClassifier(n_estimators=200,base_estimator=SVC(),algorithm='SAMME')))
    models.append(('logistic,',LogisticRegression(C=1000,solver='sag')))
    models.append(('GBDT,',GradientBoostingClassifier(max_depth=6,n_estimators=100,)))
    for clf_name,clf in models:
        clf.fit(x_train,y_train)
        xy_lst=[(x_train,y_train),(x_val,y_val),(x_test,y_test)]
        for i in range(len(xy_lst)):
            x_part=xy_lst[i][0]
            y_part=xy_lst[i][1]
            y_pred=clf.predict(x_part)
            print(i)
            print(clf_name,"acc",accuracy_score(y_part,y_pred))
            print(clf_name,'recall',recall_score(y_part,y_pred))
            print(clf_name,'f1',f1_score(y_part,y_pred))
            # dot_data=StringIO()
            #             # export_graphviz(clf,out_file=dot_data,feature_names=f_name,class_names=['NL','L'],filled=True,rounded=True,special_characters=True)
            #             # #本句代码用于画决策树；后面的filled，rounded，special_characters都是用于美化决策树图片效果的
            #             # graph=pydotplus.graph_from_dot_data(dot_data.getvalue())#本句代码用于画决策树
            #             # graph.write_pdf('dt_tree2.pdf')#本句代码用于画决策树
    # knn_clf_n5=KNeighborsClassifier(n_neighbors=4)
    # knn_clf.fit(x_train,y_train)
    # knn_clf_n5.fit(x_train,y_train)
    # y_pred=knn_clf.predict(x_val)
    # y_pred_n5=knn_clf_n5.predict(x_val)
    # print(y_pred)
    # print(y_val)
    print('——————————————')

    # print("abc",accuracy_score(y_val,y_pred))
    # print("rec:",recall_score(y_val,y_pred))
    # print("f1_score:",f1_score(y_val,y_pred))
    #下面开始使用测试集正式开始测试
    # y_pred=knn_clf.predict(x_test)
    # print("abc",float(accuracy_score(y_test, y_pred)) )
    #相似的，假如自己闲得慌也可以对训练集进行一下验证
    # y_pred_train=knn_clf.predict(x_train)
    # print("train:abc:",accuracy_score(y_train,y_pred_train))

   #将模型进行保存
    # from sklearn.externals import joblib
    # joblib.dump(knn_clf,"knn_clf")
    print("————————")
def regr_test(featrues,label):
    # print('x:',featrues)
    # print('y:',label)
    from sklearn.linear_model import LinearRegression,Ridge,Lasso#分别对应于线性回归、岭回归、Lasso回归
    # regr=LinearRegression()
    # regr=Ridge(alpha=1)
    regr=Lasso(alpha=0.001)
    regr.fit(featrues.values,label.values)
    y_pred=regr.predict(featrues.values)
    # print(y_pred)
    print('coef',regr.coef_)
    from sklearn.metrics import mean_squared_error,mean_absolute_error,r2_score
    print('MSE,',mean_squared_error(y_pred,label.values))#残差平方和
    print('MAE:',mean_absolute_error(y_pred,label.values))#残差和
    print('r2:',r2_score(y_pred,label.values))#r2值




featrues, label = hr_processing()
# featrues=pda.DataFrame(featrues)
# label=pda.DataFrame(label)
regr_test(featrues[['number_project','average_montly_hours']],featrues[['last_evaluation']])
# print(featrues,label)
# hr_modeling(featrues,label)
# from sklearn.externals import joblib
# joblib.load('knn_clf')






