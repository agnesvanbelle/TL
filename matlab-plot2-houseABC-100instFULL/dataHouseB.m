exp = [];
expLegend={};
datapoints=[];
expLegend{1}='AUTO CT\_REL PR\_BOTH KL TRANSFER';
exp(1,1)=0.22727272727272727;
datapoints(1,1)=2;

exp(1,2)=0.20224719101123595;
datapoints(2,1)=3;

exp(1,3)=0.1743486973947896;
datapoints(3,1)=6;

exp(1,4)=0.25;
datapoints(4,1)=11;



expLegend{2}='HC\_MMF TRANSFER';
exp(2,1)=0.25;
datapoints(1,1)=2;

exp(2,2)=0.23098591549295774;
datapoints(2,1)=3;

exp(2,3)=0.4059853190287973;
datapoints(3,1)=6;

exp(2,4)=0.48;
datapoints(4,1)=11;



expLegend{3}='HC\_MMF NOTRANSFER';
exp(3,1)=0.34375;
datapoints(1,1)=2;

exp(3,2)=0.30422535211267604;
datapoints(2,1)=3;

exp(3,3)=0.44776962168266515;
datapoints(3,1)=6;

exp(3,4)=0.48;
datapoints(4,1)=11;




directory='/root/source-code2c/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' B';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;