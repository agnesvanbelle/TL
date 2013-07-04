
figure('visible','on'); %set to off



[nrExperiments, nrDays] =  size(exp);

colors = getColors(nrExperiments);

lineStylesCell={'-','-.', '--',':'};
markerStylesCell={'+', 'o', '.', 'x', '*', 's', 'd'};
index=0;
legendCell = {};

for expNr=1:nrExperiments
    
    
    values = exp(expNr,:);
    
    if min(sum(isnan(values))) == 0
        
        
        plotje = semilogx(datapoints, values);
        mod(index+1, length(markerStylesCell))
        set(plotje,'LineStyle',lineStylesCell{mod(index, length(lineStylesCell))+1});
        set(plotje, 'Marker',markerStylesCell{mod(index, length(markerStylesCell))+1});
        
        set(plotje, 'MarkerFaceColor',colors(index+1,:));
        
        set(plotje,'Color',colors(index+1,:),'LineWidth',2);
        
        hold on;
        
        
        
        legendCell{index+1} = expLegend{expNr};
        
        
        %update colors, legend, line style
        index = index + 1;
        
        
        
    end
end



% fit plot
%axis tight


% set x and y axes
axis([min(datapoints),max(datapoints)+1,0,1])

% set tickmarks
set(gca,'XTick',datapoints)
set(gca,'YTick',[0:0.1:1])

% set x and y axis labels
x=xlabel('Number of labeled days');
set(x, 'FontSize', 12);
set(x,'FontWeight','bold');

y=ylabel('Avg. Accuracy');
set(y, 'FontSize', 12);
set(y,'FontWeight','bold');

% set legend
l = legend(legendCell);
set(l, 'FontSize', 10);
set(l,'FontWeight','bold');
set(l, 'Location','NorthWest');


% title
t = title(strcat('House ', houseName));
set(t, 'FontSize', 13);
set(t,'FontWeight','bold');


% remove upper and right axes
box off

%remove legend box
legend boxoff

%axis tight

% save plot
fh = gcf; % get figure handle
saveas(gcf, strcat(strcat('plot',houseName,'nofit.pdf')));
clear fh;
hold off;

