exp = [];
expLegend={};
datapoints=[];
expLegend{1}='ATRANSFERO CT\_ABS PR\_BOTH SSE ONLY\_TRANSFER';
exp(1,1)=0.43579184704184704;
datapoints(1,1)=2;

exp(1,2)=0.4285196983310189;
datapoints(2,1)=3;

exp(1,3)=0.5439883176184546;
datapoints(3,1)=6;

exp(1,4)=0.623831777890218;
datapoints(4,1)=11;

exp(1,5)=0.7035372035372035;
datapoints(5,1)=21;


expLegend{2}='ATRANSFERO CT\_REL PR\_BOTH KL ONLY\_TRANSFER';
exp(2,1)=0.4199099841956985;
datapoints(1,1)=2;

exp(2,2)=0.4477544929817657;
datapoints(2,1)=3;

exp(2,3)=0.5131409331409332;
datapoints(3,1)=6;

exp(2,4)=0.5933381823803899;
datapoints(4,1)=11;

exp(2,5)=0.6354112554112555;
datapoints(5,1)=21;


expLegend{3}='ATRANSFERO CT\_ABS PR\_SP SSE ONLY\_TRANSFER';
exp(3,1)=0.3750652786367072;
datapoints(1,1)=2;

exp(3,2)=0.39767249211693645;
datapoints(2,1)=3;

exp(3,3)=0.4110931339192211;
datapoints(3,1)=6;

exp(3,4)=0.4124991801128165;
datapoints(4,1)=11;

exp(3,5)=0.47259056732740945;
datapoints(5,1)=21;


expLegend{4}='ATRANSFERO CT\_ABS PR\_SP KL ONLY\_TRANSFER';
exp(4,1)=0.4101010101010101;
datapoints(1,1)=2;

exp(4,2)=0.4888455988455987;
datapoints(2,1)=3;

exp(4,3)=0.5489261945783683;
datapoints(3,1)=6;

exp(4,4)=0.6470371619722256;
datapoints(4,1)=11;

exp(4,5)=0.7430783397888658;
datapoints(5,1)=21;


expLegend{5}='ATRANSFERO CT\_ABS PR\_BOTH KL ONLY\_TRANSFER';
exp(5,1)=0.44931972789115643;
datapoints(1,1)=2;

exp(5,2)=0.4172769636072386;
datapoints(2,1)=3;

exp(5,3)=0.5344241794745153;
datapoints(3,1)=6;

exp(5,4)=0.6176449092033497;
datapoints(4,1)=11;

exp(5,5)=0.6938943001443002;
datapoints(5,1)=21;


expLegend{6}='ATRANSFERO CT\_REL PR\_BOTH SSE ONLY\_TRANSFER';
exp(6,1)=0.4418367346938775;
datapoints(1,1)=2;

exp(6,2)=0.43593700985005307;
datapoints(2,1)=3;

exp(6,3)=0.5397496392496389;
datapoints(3,1)=6;

exp(6,4)=0.6042420482159891;
datapoints(4,1)=11;

exp(6,5)=0.6341398680684396;
datapoints(5,1)=21;


expLegend{7}='ATRANSFERO CT\_REL PR\_SP KL ONLY\_TRANSFER';
exp(7,1)=0.43593073593073595;
datapoints(1,1)=2;

exp(7,2)=0.4185661560661559;
datapoints(2,1)=3;

exp(7,3)=0.47697440980269246;
datapoints(3,1)=6;

exp(7,4)=0.5924638427081416;
datapoints(4,1)=11;

exp(7,5)=0.6025988902277561;
datapoints(5,1)=21;


expLegend{8}='ATRANSFERO CT\_REL PR\_SP SSE ONLY\_TRANSFER';
exp(8,1)=0.4566154744726173;
datapoints(1,1)=2;

exp(8,2)=0.4249659863945576;
datapoints(2,1)=3;

exp(8,3)=0.5481975843086947;
datapoints(3,1)=6;

exp(8,4)=0.6259254183767161;
datapoints(4,1)=11;

exp(8,5)=0.6558612635932225;
datapoints(5,1)=21;



directory='/root/source-code1b/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' A';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;