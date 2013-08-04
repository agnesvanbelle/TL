exp = [];
expLegend={};
datapoints=[];

expLegend{1}='AUTO CT\_REL PR\_BOTH KL TRANSFER';
exp(1,1)=0.13253968253968257;
datapoints(1,1)=2;

exp(1,2)=0.20304834054834053;
datapoints(2,1)=3;

exp(1,3)=0.16078444269051992;
datapoints(3,1)=6;

exp(1,4)=0.20115079365079364;
datapoints(4,1)=11;



expLegend{2}='HC\_MF PR\_BOTH KL TRANSFER';
exp(2,1)=0.21409090909090905;
datapoints(1,1)=2;

exp(2,2)=0.184366391184573;
datapoints(2,1)=3;

exp(2,3)=0.22156043190525948;
datapoints(3,1)=6;

exp(2,4)=0.31878787878787873;
datapoints(4,1)=11;



expLegend{3}='HC\_MMF TRANSFER';
exp(3,1)=0.25999999999999995;
datapoints(1,1)=2;

exp(3,2)=0.20051627384960713;
datapoints(2,1)=3;

exp(3,3)=0.24746212121212116;
datapoints(3,1)=6;

exp(3,4)=0.398989898989899;
datapoints(4,1)=11;



expLegend{4}='HC\_MMF NO\_TRANSFER';
exp(4,1)=0.2933333333333333;
datapoints(1,1)=2;

exp(4,2)=0.30674523007856336;
datapoints(2,1)=3;

exp(4,3)=0.39043560606060607;
datapoints(3,1)=6;

exp(4,4)=0.4558080808080809;
datapoints(4,1)=11;




directory='./';
houseName=' B';
run saveplot;