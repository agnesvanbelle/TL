exp = [];
expLegend={};
datapoints=[];
expLegend{1}='AUTO CT\_REL PR\_BOTH KL TRANSFER';
exp(1,1)=0.14047619047619048;
datapoints(1,1)=2;

exp(1,2)=0.19413990402362497;
datapoints(2,1)=3;

exp(1,3)=0.15814512471655312;
datapoints(3,1)=6;

exp(1,4)=0.23865079365079364;
datapoints(4,1)=11;



expLegend{2}='HC\_MF PR\_BOTH KL TRANSFER';
exp(2,1)=0.2069191919191919;
datapoints(1,1)=2;

exp(2,2)=0.23145141895141894;
datapoints(2,1)=3;

exp(2,3)=0.22125893769152197;
datapoints(3,1)=6;

exp(2,4)=0.2446969696969697;
datapoints(4,1)=11;



expLegend{3}='HC\_MMF TRANSFER';
exp(3,1)=0.22055555555555556;
datapoints(1,1)=2;

exp(3,2)=0.17394879022786;
datapoints(2,1)=3;

exp(3,3)=0.388365600448934;
datapoints(3,1)=6;

exp(3,4)=0.4787878787878788;
datapoints(4,1)=11;



expLegend{4}='HC\_MMF NOTRANSFER';
exp(4,1)=0.2741666666666666;
datapoints(1,1)=2;

exp(4,2)=0.25685929058022083;
datapoints(2,1)=3;

exp(4,3)=0.42443041526374875;
datapoints(3,1)=6;

exp(4,4)=0.4558080808080809;
datapoints(4,1)=11;




directory='/root/source-code2b/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' B';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;
