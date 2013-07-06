exp = [];
expLegend={};
datapoints=[];
expLegend{1}='AUTO CT\_REL PR\_BOTH KL TRANSFER';
exp(1,1)=0.23019480519480517;
datapoints(1,1)=2;

exp(1,2)=0.23101871247032538;
datapoints(2,1)=3;

exp(1,3)=0.3072705260840854;
datapoints(3,1)=6;

exp(1,4)=0.3177740288034408;
datapoints(4,1)=11;



expLegend{2}='HC\_MF PR\_BOTH KL TRANSFER';
exp(2,1)=0.23271205918264742;
datapoints(1,1)=2;

exp(2,2)=0.2377892559211239;
datapoints(2,1)=3;

exp(2,3)=0.2926774470739989;
datapoints(3,1)=6;

exp(2,4)=0.31973458640125313;
datapoints(4,1)=11;



expLegend{3}='HC\_MMF TRANSFER';
exp(3,1)=0.29252446899505724;
datapoints(1,1)=2;

exp(3,2)=0.29328406416035263;
datapoints(2,1)=3;

exp(3,3)=0.3346880726982771;
datapoints(3,1)=6;

exp(3,4)=0.41535227939448743;
datapoints(4,1)=11;



expLegend{4}='HC\_MMF NOTRANSFER';
exp(4,1)=0.2852846499905324;
datapoints(1,1)=2;

exp(4,2)=0.3176430385193271;
datapoints(2,1)=3;

exp(4,3)=0.3817925555170457;
datapoints(3,1)=6;

exp(4,4)=0.40376276861666516;
datapoints(4,1)=11;




directory='/root/source-code2b/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' C';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;
