import numpy as np 
import pandas as pd 
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.svm import SVC
from sklearn.neighbors import KNeighborsClassifier
from sklearn.preprocessing import StandardScaler, LabelEncoder
from sklearn.ensemble import VotingClassifier
from sklearn.metrics import roc_curve, auc
import matplotlib.pyplot as plt

df = pd.read_csv('./heart.csv)
print(df.head(20)
for column in df.columns:
    unique_values = df[column].unique()
    print(f"Unique values in '{column}': {unique_values}")

#columns:Index(['Age', 'Sex', 'ChestPainType', 'RestingBP', 'Cholesterol', 'FastingBS',
#        'RestingECG', 'MaxHR', 'ExerciseAngina', 'Oldpeak', 'ST_Slope',
#        'HeartDisease'],
#       dtype='object')

print(df.isnull().sum())

'''
Since some values require specific knowledge for understanding I googled to attain the required knowledge and 
SubStitue right numerical values to develop better model.

"Sex" and "ExerciseAngina" columns are simply handled by sklearn.labelencoder

1.ChestPaintype
Severity: ASY< NAP < ATA < TA

2.RestingECG
Severity: Normal < ST < LVH

3.ST-Slope
Severity: Up< Flat < Down
'''


new_df = df.copy
encoder = LabelEncoder()
encoder.fit(df["Sex"])
df["Sex"] = encoder.transform(df["Sex"])
encoder.fit(df["ExerciseAngina"])
df["ExerciseAngina"] = encoder.transform(df["ExerciseAngina"])


ChestPainTypemap = {"ASY":0, "NAP":1 , "ATA":2 , "TA":3}
RestingECGmap = {"Normal": 0, "ST":1 , "LVH":2}
STSlopemap = {"Up":0, "Flat":1, "Down":2}


df["ChestPainType"] = df["ChestPainType"].map(ChestPainTypemap)
df["RestingECG"] = df["RestingECG"].map(RestingECGmap)
df["ST_Slope"] = df["ST_Slope"].map(STSlopemap)

X, y = df.iloc[:,:-1], df.iloc[:, -1]
X_train, X_test, y_train, y_test = train_test_split(X, y , test_size = 0.3, shuffle = True , random_state = 1)
std_scaler = StandardScaler()
std_scaler.fit(X_train)
X_train_std = std_scaler.transform(X_train)
X_test_std = std_scaler.transform(X_test)
print("X_train_std: ", np.mean(X_test_std))
print("X_train_std: ", np.std(X_test_std))
# plt.plot([i in range(X_train_std.shape[0])], X_train_std[:,])
sns.heatmap(data = df,cmap='YlGnBu')
plt.show()

from sklearn.model_selection import RandomizedSearchCV
#build models
clf_lr = LogisticRegression(max_iter=1000)
clf_svm = SVC()
clf_kn = KNeighborsClassifier(n_neighbors=5)
param_range = [0.001*(10**i) for i in range(0,6)]
parameters_lr = [
    {"solver":["lbfgs"], "C" : param_range },{"solver":["liblinear"]}
]

parameters_svm = [
    {"kernel":["rbf"], "C":param_range, "gamma":param_range},
    {"kernel":["polynomial"],"C":param_range, "gamma":param_range}
]

parameters_kn = [
    {"n_neighbors":[5],"algorithm":["ball_tree","kd_tree"]},{"n_neighbors":[3],"algorithm":["ball_tree","kd_tree"]}
]

rs_lr = RandomizedSearchCV(estimator = clf_lr ,param_distributions = parameters_lr
                        , scoring = "accuracy", cv=10 , random_state=42)
rs_lr.fit(X_train_std, y_train)
print(f"Best Score in Logistic Regression:{rs_kn.best_score_:.3f}")
rs_svm = RandomizedSearchCV(estimator = clf_svm ,param_distributions = parameters_svm
                        , scoring = "accuracy", cv=10 , random_state=42)
rs_svm.fit(X_train_std, y_train)
print(f"Best Score in Support Vector Machine:{rs_kn.best_score_:.3f}")

rs_kn = RandomizedSearchCV(estimator = clf_kn ,param_distributions = parameters_kn
                        , scoring = "accuracy", cv=10 , random_state=42)
rs_kn.fit(X_train_std, y_train)
print(f"Best Score in Logistic K-Neighbors:{rs_kn.best_score_:.3f}")
#got 0.849 for all models


# Predict probabilities
probs = rs_lr.predict_proba(X_test_std)[:, 1]
# Compute ROC curve and AUC
fpr, tpr, thresholds = roc_curve(y_test, probs)
roc_auc = auc(fpr, tpr)

#plot
plt.figure()
plt.plot(fpr, tpr, color='darkorange', lw=2, label=f'ROC curve (area = {roc_auc:.2f})')
plt.plot([0, 1], [0, 1], color='navy', lw=2, linestyle='--')
plt.xlim([0.0, 1.0])
plt.ylim([0.0, 1.05])
plt.xlabel('False Positive Rate')
plt.ylabel('True Positive Rate')
plt.title('Receiver Operating Characteristic')
plt.legend(loc="lower right")
plt.show()

# Using best classifiers from LogisticRegression, SupportVectorMachine and KnearestNeighbors from RandomizedSearch
# Ensemblevoting
clf_lr_best = LogisticRegression(**rs_lr.best_params_, max_iter=1000)
clf_svm_best = SVC(**rs_svm.best_params_, probability = True)
clf_kn_best = KNeighborsClassifier(**rs_kn.best_params_)
voting_clf = VotingClassifier(
    estimators=[('lr', clf_lr_best), ('svm', clf_svm_best), ('knn', clf_kn_best)],
    voting='soft'  # or 'hard'
)
voting_clf.fit(X_train_std, y_train)
ensemble_score = voting_clf.score(X_test_std, y_test)
print("Ensemble model accuracy:", ensemble_score)

