exp = [];
expLegend={};
datapoints=[];
expLegend{1}='AUTO CT\_REL PR\_BOTH KL TRANSFER';
exp(1,1)=0.24561403508771928;
datapoints(1,1)=2;

exp(1,2)=0.19801980198019803;
datapoints(2,1)=3;

exp(1,3)=0.16759002770083103;
datapoints(3,1)=6;

exp(1,4)=0.24193548387096775;
datapoints(4,1)=11;



expLegend{2}='HC\_MMF TRANSFER';
exp(2,1)=0.3150684931506849;
datapoints(1,1)=2;

exp(2,2)=0.24855491329479767;
datapoints(2,1)=3;

exp(2,3)=0.41379310344827586;
datapoints(3,1)=6;

exp(2,4)=0.4632352941176471;
datapoints(4,1)=11;



expLegend{3}='HC\_MMF NOTRANSFER';
exp(3,1)=0.3698630136986301;
datapoints(1,1)=2;

exp(3,2)=0.29190751445086704;
datapoints(2,1)=3;

exp(3,3)=0.44170771756978655;
datapoints(3,1)=6;

exp(3,4)=0.5073529411764706;
datapoints(4,1)=11;




directory='/root/source-code2c/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' B';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;