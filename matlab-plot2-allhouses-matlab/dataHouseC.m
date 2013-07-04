exp = [];
expLegend={};
datapoints=[];
expLegend{1}='ATRANSFERO CT\_REL PR\_BOTH KL ONLY\_TRANSFER';
exp(1,1)=0.2213585434173669;
datapoints(1,1)=2;

exp(1,2)=0.25577849117174956;
datapoints(2,1)=3;

exp(1,3)=0.29723154362416115;
datapoints(3,1)=6;

exp(1,4)=0.3652850396356895;
datapoints(4,1)=11;



expLegend{2}='HC TRANSFER\_REL PR\_BOTH KL ONLY\_TRANSFER';
exp(2,1)=0.2795266824678589;
datapoints(1,1)=2;

exp(2,2)=0.2748811373811372;
datapoints(2,1)=3;

exp(2,3)=0.28431846898745855;
datapoints(3,1)=6;

exp(2,4)=0.29608792401952;
datapoints(4,1)=11;



expLegend{3}='HTRANSFERMMF BOTH';
exp(3,1)=0.25898807075277663;
datapoints(1,1)=2;

exp(3,2)=0.27012261465386467;
datapoints(2,1)=3;

exp(3,3)=0.3122118215868218;
datapoints(3,1)=6;

exp(3,4)=0.3739725053450546;
datapoints(4,1)=11;



expLegend{4}='HNOTRANSFERMMF BOTH';
exp(4,1)=0.2698154786390081;
datapoints(1,1)=2;

exp(4,2)=0.3036766474266473;
datapoints(2,1)=3;

exp(4,3)=0.3817202973452977;
datapoints(3,1)=6;

exp(4,4)=0.4217270928055243;
datapoints(4,1)=11;




directory='/root/source-code/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' C';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;