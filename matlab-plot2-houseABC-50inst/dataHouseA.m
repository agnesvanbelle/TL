exp = [];
expLegend={};
datapoints=[];
expLegend{1}='AUTO CT\_REL PR\_BOTH KL TRANSFER';
exp(1,1)=0.4500017178588608;
datapoints(1,1)=2;

exp(1,2)=0.44507592936164353;
datapoints(2,1)=3;

exp(1,3)=0.5568450255318937;
datapoints(3,1)=6;

exp(1,4)=0.6242058806993867;
datapoints(4,1)=11;

exp(1,5)=0.6429288304288308;
datapoints(5,1)=21;


expLegend{2}='HC\_MF PR\_BOTH KL TRANSFER';
exp(2,1)=0.4116885759742902;
datapoints(1,1)=2;

exp(2,2)=0.43412150588874704;
datapoints(2,1)=3;

exp(2,3)=0.5085489232989235;
datapoints(3,1)=6;

exp(2,4)=0.6199521888645255;
datapoints(4,1)=11;

exp(2,5)=0.6025098121251966;
datapoints(5,1)=21;


expLegend{3}='HC\_MMF TRANSFER';
exp(3,1)=0.45302660302660286;
datapoints(1,1)=2;

exp(3,2)=0.46284849625971103;
datapoints(2,1)=3;

exp(3,3)=0.5668512968512961;
datapoints(3,1)=6;

exp(3,4)=0.6135832061156725;
datapoints(4,1)=11;

exp(3,5)=0.6490765082870347;
datapoints(5,1)=21;


expLegend{4}='HC\_MMF NOTRANSFER';
exp(4,1)=0.5104379747236889;
datapoints(1,1)=2;

exp(4,2)=0.5228861999890037;
datapoints(2,1)=3;

exp(4,3)=0.5955113220113213;
datapoints(3,1)=6;

exp(4,4)=0.6421508686119068;
datapoints(4,1)=11;

exp(4,5)=0.6588408667356034;
datapoints(5,1)=21;



directory='/root/source-code2b/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' A';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;
