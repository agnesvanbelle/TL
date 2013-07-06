exp = [];
expLegend={};
datapoints=[];
expLegend{1}='ATRANSFERO CT\_REL PR\_BOTH KL ONLY\_TRANSFER';
exp(1,1)=0.16590964590964588;
datapoints(1,1)=2;

exp(1,2)=0.23118991485510185;
datapoints(2,1)=3;

exp(1,3)=0.23996153811849655;
datapoints(3,1)=6;

exp(1,4)=0.2405919248609312;
datapoints(4,1)=11;



expLegend{2}='HC TRANSFER\_REL PR\_BOTH KL ONLY\_TRANSFER';
exp(2,1)=0.2758018833018833;
datapoints(1,1)=2;

exp(2,2)=0.23917764711309392;
datapoints(2,1)=3;

exp(2,3)=0.33610401262023964;
datapoints(3,1)=6;

exp(2,4)=0.36072011217584554;
datapoints(4,1)=11;



expLegend{3}='HTRANSFERMMF BOTH';
exp(3,1)=0.2608251724195997;
datapoints(1,1)=2;

exp(3,2)=0.2866084516942154;
datapoints(2,1)=3;

exp(3,3)=0.37615846337646003;
datapoints(3,1)=6;

exp(3,4)=0.4168944494997401;
datapoints(4,1)=11;



expLegend{4}='HNOTRANSFERMMF BOTH';
exp(4,1)=0.3047272478696627;
datapoints(1,1)=2;

exp(4,2)=0.306788856278665;
datapoints(2,1)=3;

exp(4,3)=0.37594777222739445;
datapoints(3,1)=6;

exp(4,4)=0.40528771332346625;
datapoints(4,1)=11;




directory='/root/source-code/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' D';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;