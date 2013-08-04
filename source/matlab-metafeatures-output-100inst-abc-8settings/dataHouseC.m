exp = [];
expLegend={};
datapoints=[];
expLegend{1}='CT\_ABS PR\_BOTH SSE';
exp(1,1)=0.20235336558865966;
datapoints(1,1)=2;

exp(1,2)=0.24994484395310831;
datapoints(2,1)=3;

exp(1,3)=0.27718116362446266;
datapoints(3,1)=6;

exp(1,4)=0.3471145754712888;
datapoints(4,1)=11;



expLegend{2}='CT\_REL PR\_BOTH KL';
exp(2,1)=0.24816017316017316;
datapoints(1,1)=2;

exp(2,2)=0.2418015185872329;
datapoints(2,1)=3;

exp(2,3)=0.26167851353036575;
datapoints(3,1)=6;

exp(2,4)=0.2859098312820648;
datapoints(4,1)=11;



expLegend{3}='CT\_ABS PR\_SP SSE';
exp(3,1)=0.1927022324081148;
datapoints(1,1)=2;

exp(3,2)=0.21301451280959463;
datapoints(2,1)=3;

exp(3,3)=0.25036253551717513;
datapoints(3,1)=6;

exp(3,4)=0.35248752793484456;
datapoints(4,1)=11;



expLegend{4}='CT\_ABS PR\_SP KL';
exp(4,1)=0.19233935998641882;
datapoints(1,1)=2;

exp(4,2)=0.2334042809042809;
datapoints(2,1)=3;

exp(4,3)=0.2858061400155858;
datapoints(3,1)=6;

exp(4,4)=0.3377722874734829;
datapoints(4,1)=11;



expLegend{5}='CT\_ABS PR\_BOTH KL';
exp(5,1)=0.2470715558950853;
datapoints(1,1)=2;

exp(5,2)=0.2273736767434246;
datapoints(2,1)=3;

exp(5,3)=0.28219696969696983;
datapoints(3,1)=6;

exp(5,4)=0.3493485020270735;
datapoints(4,1)=11;



expLegend{6}='CT\_REL PR\_BOTH SSE';
exp(6,1)=0.2693086325439267;
datapoints(1,1)=2;

exp(6,2)=0.23117708562623815;
datapoints(2,1)=3;

exp(6,3)=0.26269766269766304;
datapoints(3,1)=6;

exp(6,4)=0.2904335040789228;
datapoints(4,1)=11;



expLegend{7}='CT\_REL PR\_SP KL';
exp(7,1)=0.18332696715049654;
datapoints(1,1)=2;

exp(7,2)=0.24446206248531818;
datapoints(2,1)=3;

exp(7,3)=0.3062578514039895;
datapoints(3,1)=6;

exp(7,4)=0.30617613206898925;
datapoints(4,1)=11;



expLegend{8}='CT\_REL PR\_SP SSE';
exp(8,1)=0.26079704609116366;
datapoints(1,1)=2;

exp(8,2)=0.20138631210059774;
datapoints(2,1)=3;

exp(8,3)=0.24612358706108733;
datapoints(3,1)=6;

exp(8,4)=0.24580207309291022;
datapoints(4,1)=11;




directory='/root/source-code1c/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' C (transfer)';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;