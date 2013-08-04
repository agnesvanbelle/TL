exp = [];
expLegend={};
datapoints=[];
expLegend{1}='ATRANSFERO CT\_ABS PR\_BOTH SSE ONLY\_TRANSFER';
exp(1,1)=0.1877435064935065;
datapoints(1,1)=2;

exp(1,2)=0.24538161957516796;
datapoints(2,1)=3;

exp(1,3)=0.2699269325557986;
datapoints(3,1)=6;

exp(1,4)=0.35718818519143186;
datapoints(4,1)=11;



expLegend{2}='ATRANSFERO CT\_REL PR\_BOTH KL ONLY\_TRANSFER';
exp(2,1)=0.19208683473389354;
datapoints(1,1)=2;

exp(2,2)=0.25905693144065234;
datapoints(2,1)=3;

exp(2,3)=0.2833013075360015;
datapoints(3,1)=6;

exp(2,4)=0.284632034632035;
datapoints(4,1)=11;



expLegend{3}='ATRANSFERO CT\_ABS PR\_SP SSE ONLY\_TRANSFER';
exp(3,1)=0.18277947542653422;
datapoints(1,1)=2;

exp(3,2)=0.2813085744664691;
datapoints(2,1)=3;

exp(3,3)=0.38198538283862493;
datapoints(3,1)=6;

exp(3,4)=0.43395741970660506;
datapoints(4,1)=11;



expLegend{4}='ATRANSFERO CT\_ABS PR\_SP KL ONLY\_TRANSFER';
exp(4,1)=0.1410618792971734;
datapoints(1,1)=2;

exp(4,2)=0.2080266955266955;
datapoints(2,1)=3;

exp(4,3)=0.2857791723563537;
datapoints(3,1)=6;

exp(4,4)=0.34087994886040485;
datapoints(4,1)=11;



expLegend{5}='ATRANSFERO CT\_ABS PR\_BOTH KL ONLY\_TRANSFER';
exp(5,1)=0.14677234530175703;
datapoints(1,1)=2;

exp(5,2)=0.26225166951911144;
datapoints(2,1)=3;

exp(5,3)=0.26754145508470784;
datapoints(3,1)=6;

exp(5,4)=0.3568632166671382;
datapoints(4,1)=11;



expLegend{6}='ATRANSFERO CT\_REL PR\_BOTH SSE ONLY\_TRANSFER';
exp(6,1)=0.17918258212375857;
datapoints(1,1)=2;

exp(6,2)=0.22188483864620226;
datapoints(2,1)=3;

exp(6,3)=0.27697870957256626;
datapoints(3,1)=6;

exp(6,4)=0.28536406216734084;
datapoints(4,1)=11;



expLegend{7}='ATRANSFERO CT\_REL PR\_SP KL ONLY\_TRANSFER';
exp(7,1)=0.2389164756811816;
datapoints(1,1)=2;

exp(7,2)=0.21383208989591973;
datapoints(2,1)=3;

exp(7,3)=0.27338689559028556;
datapoints(3,1)=6;

exp(7,4)=0.3174772939478821;
datapoints(4,1)=11;



expLegend{8}='ATRANSFERO CT\_REL PR\_SP SSE ONLY\_TRANSFER';
exp(8,1)=0.27610771581359816;
datapoints(1,1)=2;

exp(8,2)=0.2411659768802625;
datapoints(2,1)=3;

exp(8,3)=0.30986215538847134;
datapoints(3,1)=6;

exp(8,4)=0.31416793990323405;
datapoints(4,1)=11;




directory='/root/source-code1b/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' C';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;