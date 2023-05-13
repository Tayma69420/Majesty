/*import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.util.Resources;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Display;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.models.MultipleCategorySeries;
import com.codename1.charts.views.BarChart;
import com.codename1.charts.views.ChartView;
import com.codename1.charts.views.XYChart;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import java.util.HashMap;
import java.util.Map;

public class Charts extends Form {
    private final Resources theme;

    public Charts() {
        this(new BorderLayout());
    }

    public Charts(BorderLayout borderLayout) {
        super(borderLayout);
        this.theme = UIManager.initFirstTheme("/theme");

        // Instantiate the reclamation repository
        ReclamationRepository reclamationRepository = new ReclamationRepository();

        // Create a map to store the ratings and their counts
        Map<Integer, Integer> ratings = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            ratings.put(i, 0);
        }

        // Count the number of reclamations for each rating
        for (Reclamation reclamation : reclamationRepository.findAll()) {
            int rating = reclamation.getRating();
            if (rating >= 1 && rating <= 5) {
                ratings.put(rating, ratings.get(rating) + 1);
            }
        }

        // Create the chart data
        String[] categories = {"1", "2", "3", "4", "5"};
        CategorySeries series = new CategorySeries("");
        for (int i = 0; i < categories.length; i++) {
            series.add(categories[i], ratings.get(i + 1));
        }

        // Create the chart
        BarChart chart = new BarChart(new MultipleCategorySeries(series), new DefaultRenderer());
        chart.setTitle("Reclamation Ratings");
        chart.setBackgroundColor(ColorUtil.WHITE);
        chart.setBarSpacing(10);
        chart.setBarWidth(50);

        // Add the chart to the form
        ChartView chartView = new ChartView(chart);
        this.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        this.addComponent(chartView);
    }
}
*/