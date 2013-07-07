exp = [];
expLegend={};
datapoints=[];
expLegend{1}='AUTO CT\_REL PR\_BOTH KL TRANSFER';
exp(1,1)=0.2679738562091503;
datapoints(1,1)=2;

exp(1,2)=0.2467005076142132;
datapoints(2,1)=3;

exp(1,3)=0.318934787167121;
datapoints(3,1)=6;

exp(1,4)=0.3217155143148127;
datapoints(4,1)=11;



expLegend{2}='HC\_MMF TRANSFER';
exp(2,1)=0.2569444444444444;
datapoints(1,1)=2;

exp(2,2)=0.29110512129380056;
datapoints(2,1)=3;

exp(2,3)=0.34656431379891406;
datapoints(3,1)=6;

exp(2,4)=0.4104882459312839;
datapoints(4,1)=11;



expLegend{3}='HC\_MMF NOTRANSFER';
exp(3,1)=0.25;
datapoints(1,1)=2;

exp(3,2)=0.3288409703504043;
datapoints(2,1)=3;

exp(3,3)=0.41321849840853775;
datapoints(3,1)=6;

exp(3,4)=0.44062688366485836;
datapoints(4,1)=11;




directory='/root/source-code2c/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' C';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;