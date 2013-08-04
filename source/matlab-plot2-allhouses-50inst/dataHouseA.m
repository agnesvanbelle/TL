exp = [];
expLegend={};
datapoints=[];
expLegend{1}='AUTO CT\_REL PR\_BOTH KL TRANSFER';
exp(1,1)=0.4622414622414622;
datapoints(1,1)=2;

exp(1,2)=0.4169269222840648;
datapoints(2,1)=3;

exp(1,3)=0.4405829580072006;
datapoints(3,1)=6;

exp(1,4)=0.5233779159674923;
datapoints(4,1)=11;

exp(1,5)=0.6299545982472813;
datapoints(5,1)=21;


expLegend{2}='HC\_MF PR\_BOTH KL TRANSFER';
exp(2,1)=0.4262221905079048;
datapoints(1,1)=2;

exp(2,2)=0.4283969146118677;
datapoints(2,1)=3;

exp(2,3)=0.4264106759090035;
datapoints(3,1)=6;

exp(2,4)=0.5093518584590008;
datapoints(4,1)=11;

exp(2,5)=0.6272930655842046;
datapoints(5,1)=21;


expLegend{3}='HC\_MMF TRANSFER';
exp(3,1)=0.4370082298653727;
datapoints(1,1)=2;

exp(3,2)=0.41585163807386005;
datapoints(2,1)=3;

exp(3,3)=0.45657775970276;
datapoints(3,1)=6;

exp(3,4)=0.5333101422387134;
datapoints(4,1)=11;

exp(3,5)=0.6442376337113178;
datapoints(5,1)=21;


expLegend{4}='HC\_MMF NO\_TRANSFER';
exp(4,1)=0.4797284725856154;
datapoints(1,1)=2;

exp(4,2)=0.5109078421578421;
datapoints(2,1)=3;

exp(4,3)=0.6061814499314493;
datapoints(3,1)=6;

exp(4,4)=0.6396158224891985;
datapoints(4,1)=11;

exp(4,5)=0.6626840021576862;
datapoints(5,1)=21;



directory='./';
houseName=' A';
run saveplot;