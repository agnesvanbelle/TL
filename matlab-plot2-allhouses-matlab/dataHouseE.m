exp = [];
expLegend={};
datapoints=[];
expLegend{1}='ATRANSFERO CT\_REL PR\_BOTH KL ONLY\_TRANSFER';
exp(1,1)=0.22452084952084952;
datapoints(1,1)=2;

exp(1,2)=0.26512043794178863;
datapoints(2,1)=3;

exp(1,3)=0.28213818119950806;
datapoints(3,1)=6;

exp(1,4)=0.2862125669110962;
datapoints(4,1)=11;



expLegend{2}='HC TRANSFER\_REL PR\_BOTH KL ONLY\_TRANSFER';
exp(2,1)=0.2669325881090587;
datapoints(1,1)=2;

exp(2,2)=0.2885301972279524;
datapoints(2,1)=3;

exp(2,3)=0.382018482530883;
datapoints(3,1)=6;

exp(2,4)=0.42618587711363126;
datapoints(4,1)=11;



expLegend{3}='HTRANSFERMMF BOTH';
exp(3,1)=0.22483545865898805;
datapoints(1,1)=2;

exp(3,2)=0.29619563546406424;
datapoints(2,1)=3;

exp(3,3)=0.3906350631210824;
datapoints(3,1)=6;

exp(3,4)=0.41269243811578954;
datapoints(4,1)=11;



expLegend{4}='HNOTRANSFERMMF BOTH';
exp(4,1)=0.22692462765992177;
datapoints(1,1)=2;

exp(4,2)=0.289025151236618;
datapoints(2,1)=3;

exp(4,3)=0.3746117079666167;
datapoints(3,1)=6;

exp(4,4)=0.4134943809409052;
datapoints(4,1)=11;




directory='/root/source-code/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' E';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;