exp = [];
expLegend={};
datapoints=[];
expLegend{1}='AUTO CT\_REL PR\_BOTH KL TRANSFER';
exp(1,1)=0.4093567251461988;
datapoints(1,1)=2;

exp(1,2)=0.44977843426883307;
datapoints(2,1)=3;

exp(1,3)=0.5515336254524671;
datapoints(3,1)=6;

exp(1,4)=0.625;
datapoints(4,1)=11;

exp(1,5)=0.6323616894705533;
datapoints(5,1)=21;


expLegend{2}='HC\_MMF TRANSFER';
exp(2,1)=0.4117647058823529;
datapoints(1,1)=2;

exp(2,2)=0.48249027237354086;
datapoints(2,1)=3;

exp(2,3)=0.5585729499467519;
datapoints(3,1)=6;

exp(2,4)=0.6133067955644015;
datapoints(4,1)=11;

exp(2,5)=0.6374594343996292;
datapoints(5,1)=21;


expLegend{3}='HC\_MMF NOTRANSFER';
exp(3,1)=0.48128342245989303;
datapoints(1,1)=2;

exp(3,2)=0.522697795071336;
datapoints(2,1)=3;

exp(3,3)=0.5965566205182818;
datapoints(3,1)=6;

exp(3,4)=0.6370012321107004;
datapoints(4,1)=11;

exp(3,5)=0.6476587853500232;
datapoints(5,1)=21;



directory='/root/source-code2c/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' A';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;