exp = [];
expLegend={};
datapoints=[];
expLegend{1}='AUTO CT\_REL PR\_BOTH KL TRANSFER';
exp(1,1)=0.1956521739130435;
datapoints(1,1)=2;

exp(1,2)=0.2649484536082474;
datapoints(2,1)=3;

exp(1,3)=0.32105599318714073;
datapoints(3,1)=6;

exp(1,4)=0.31112661318451346;
datapoints(4,1)=11;



expLegend{2}='HC\_MMF TRANSFER';
exp(2,1)=0.22699386503067484;
datapoints(1,1)=2;

exp(2,2)=0.31400535236396077;
datapoints(2,1)=3;

exp(2,3)=0.3475854170643253;
datapoints(3,1)=6;

exp(2,4)=0.4096638655462185;
datapoints(4,1)=11;



expLegend{3}='HC\_MMF NOTRANSFER';
exp(3,1)=0.2331288343558282;
datapoints(1,1)=2;

exp(3,2)=0.3264942016057092;
datapoints(2,1)=3;

exp(3,3)=0.4035121206337087;
datapoints(3,1)=6;

exp(3,4)=0.4338735494197679;
datapoints(4,1)=11;




directory='/root/source-code2c/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' C';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;