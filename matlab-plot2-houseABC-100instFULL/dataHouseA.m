exp = [];
expLegend={};
datapoints=[];
expLegend{1}='AUTO CT\_REL PR\_BOTH KL TRANSFER';
exp(1,1)=0.4223300970873786;
datapoints(1,1)=2;

exp(1,2)=0.44482522275531183;
datapoints(2,1)=3;

exp(1,3)=0.5548547400611621;
datapoints(3,1)=6;

exp(1,4)=0.6227662431566987;
datapoints(4,1)=11;

exp(1,5)=0.6473537604456825;
datapoints(5,1)=21;


expLegend{2}='HC\_MMF TRANSFER';
exp(2,1)=0.4549763033175355;
datapoints(1,1)=2;

exp(2,2)=0.48129851799576573;
datapoints(2,1)=3;

exp(2,3)=0.5637403897729304;
datapoints(3,1)=6;

exp(2,4)=0.6186512815662537;
datapoints(4,1)=11;

exp(2,5)=0.6243321002877107;
datapoints(5,1)=21;


expLegend{3}='HC\_MMF NOTRANSFER';
exp(3,1)=0.4881516587677725;
datapoints(1,1)=2;

exp(3,2)=0.5208186309103741;
datapoints(2,1)=3;

exp(3,3)=0.5843018058287145;
datapoints(3,1)=6;

exp(3,4)=0.6404048046911945;
datapoints(4,1)=11;

exp(3,5)=0.6489930127414715;
datapoints(5,1)=21;



directory='/root/source-code2c/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' A';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;