exp = [];
expLegend={};
datapoints=[];
expLegend{1}='ATRANSFERO CT\_ABS PR\_BOTH SSE ONLY\_TRANSFER';
exp(1,1)=0.23007936507936505;
datapoints(1,1)=2;

exp(1,2)=0.17407407407407408;
datapoints(2,1)=3;

exp(1,3)=0.18455841638981155;
datapoints(3,1)=6;

exp(1,4)=0.18686507936507937;
datapoints(4,1)=11;



expLegend{2}='ATRANSFERO CT\_REL PR\_BOTH KL ONLY\_TRANSFER';
exp(2,1)=0.14999999999999997;
datapoints(1,1)=2;

exp(2,2)=0.181708238851096;
datapoints(2,1)=3;

exp(2,3)=0.19310064935064924;
datapoints(3,1)=6;

exp(2,4)=0.18686507936507937;
datapoints(4,1)=11;



expLegend{3}='ATRANSFERO CT\_ABS PR\_SP SSE ONLY\_TRANSFER';
exp(3,1)=0.24932539682539678;
datapoints(1,1)=2;

exp(3,2)=0.18777056277056275;
datapoints(2,1)=3;

exp(3,3)=0.17035082972582957;
datapoints(3,1)=6;

exp(3,4)=0.16453634085213031;
datapoints(4,1)=11;



expLegend{4}='ATRANSFERO CT\_ABS PR\_SP KL ONLY\_TRANSFER';
exp(4,1)=0.14285714285714285;
datapoints(1,1)=2;

exp(4,2)=0.1704906204906205;
datapoints(2,1)=3;

exp(4,3)=0.19579715864246236;
datapoints(3,1)=6;

exp(4,4)=0.20115079365079364;
datapoints(4,1)=11;



expLegend{5}='ATRANSFERO CT\_ABS PR\_BOTH KL ONLY\_TRANSFER';
exp(5,1)=0.20793650793650792;
datapoints(1,1)=2;

exp(5,2)=0.1244318181818182;
datapoints(2,1)=3;

exp(5,3)=0.18271193092621643;
datapoints(3,1)=6;

exp(5,4)=0.18686507936507937;
datapoints(4,1)=11;



expLegend{6}='ATRANSFERO CT\_REL PR\_BOTH SSE ONLY\_TRANSFER';
exp(6,1)=0.12222222222222223;
datapoints(1,1)=2;

exp(6,2)=0.17672572905131043;
datapoints(2,1)=3;

exp(6,3)=0.1740044555834028;
datapoints(3,1)=6;

exp(6,4)=0.18686507936507937;
datapoints(4,1)=11;



expLegend{7}='ATRANSFERO CT\_REL PR\_SP KL ONLY\_TRANSFER';
exp(7,1)=0.20753968253968252;
datapoints(1,1)=2;

exp(7,2)=0.21676587301587297;
datapoints(2,1)=3;

exp(7,3)=0.14775698158051087;
datapoints(3,1)=6;

exp(7,4)=0.19757936507936505;
datapoints(4,1)=11;



expLegend{8}='ATRANSFERO CT\_REL PR\_SP SSE ONLY\_TRANSFER';
exp(8,1)=0.26186507936507936;
datapoints(1,1)=2;

exp(8,2)=0.1499911816578483;
datapoints(2,1)=3;

exp(8,3)=0.19411878881987574;
datapoints(3,1)=6;

exp(8,4)=0.17436507936507936;
datapoints(4,1)=11;




directory='/root/source-code1b/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' B';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;