package it.triviapatente.android.app.views.stats;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.DimenRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.ReceivedData;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;
import it.triviapatente.android.app.utils.custom_classes.callbacks.QuizSheetCallback;
import it.triviapatente.android.app.utils.custom_classes.listViews.expandable_list.TPExpandableList;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.footer.TPEmoticonFooter;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.QuestionHolder;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.QuizHolder;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.RecentGameHolder;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.base.Base;
import it.triviapatente.android.models.game.Category;
import it.triviapatente.android.models.game.Game;
import it.triviapatente.android.models.game.Quiz;
import it.triviapatente.android.models.responses.CategoryDetail;
import it.triviapatente.android.models.stats.Progress;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatDetailActivity extends TPActivity {

    @BindView(R.id.progressTextView) TextView progressTextView;
    @BindView(R.id.hintTextView) TextView hintTextView;
    @BindView(R.id.chart) LineChart chart;
    @BindView(R.id.wrongAnswersSheet) View wrongAnswersBottomSheet;
    @DimenRes int progressPercentageSizeRes = R.dimen.progressPercentageSize;
    @DimenRes int progressTextSizeRes = R.dimen.progressTextSize;
    @BindString(R.string.wrong_answer_list_title_empty) String wrongAnswerListTitleEmpty;
    @BindString(R.string.wrong_answer_list_title_full) String wrongAnswerListTitleFull;
    @BindDimen(R.dimen.wrong_answer_height) int wrongAnswerHeight;

    int progressPercentageSize, progressTextSize;

    private TPExpandableList<Quiz> wrongAnswersList;

    private Category category;
    private CategoryDetail categoryDetail;
    private List<String> keys = null;

    private void getCategory() {
        String categoryString = getIntent().getStringExtra(getString(R.string.extra_string_category));
        category = RetrofitManager.gson.fromJson(categoryString, Category.class);
    }
    private List<String> getKeys() {
        if(keys != null) return keys;
        Set<String> keySet = categoryDetail.progressMap.keySet();
        return keys = new ArrayList<>(keySet);
    }

    private DateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
    private DateFormat humanReadableFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
    IAxisValueFormatter formatter = new IAxisValueFormatter() {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            String key = getKeys().get((int)value);
            Progress progress = categoryDetail.progressMap.get(key);
            try {
                Date date = isoFormat.parse(key);
                int errors = progress.numberOfErrors();
                String suffix = "";
                if(errors > 0) {
                    suffix = "\n" + getString(errors == 1 ? R.string.stats_error_singular : R.string.stats_error_plural).replace("%d", String.valueOf(errors));
                }
                return humanReadableFormat.format(date) + suffix;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "";
        }
    };
    IValueFormatter decimalFormatter = new IValueFormatter() {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if(entry.getData() == null) return ""; //ghost entry
            return String.valueOf((int)value);
        }
    };
    private void setDataToChart() {
        List<String> keys = getKeys();
        chart.setData(getLineData(keys, categoryDetail.progressMap));
        chart.setVisibleXRangeMaximum(5);
        chart.moveViewToX(keys.size() - 1);
        chart.invalidate();
    }
    private LineData getLineData(List<String> keys, Map<String, Progress> map) {
        List<ILineDataSet> sets = new ArrayList<>();
        List<Entry> badEntries = new ArrayList<>();
        List<Entry> mediumEntries = new ArrayList<>();
        List<Entry> goodEntries = new ArrayList<>();
        List<Entry> perfectEntries = new ArrayList<>();
        for(float n = 0; n < keys.size(); n += 0.5) {
            Entry ghostEntry = new Entry(n, 0);
            ghostEntry.setData(null);
            int intN = Math.round(n);
            if(intN == n) {
                String key = keys.get(intN);
                Progress progress = categoryDetail.progressMap.get(key);
                Entry entry = new Entry(n, progress.totalAnswers);
                entry.setData(progress);
                float percent = progress.getPercent();
                badEntries.add((percent <= 25) ? entry : ghostEntry);
                mediumEntries.add((percent > 25 && percent <= 50) ? entry : ghostEntry);
                goodEntries.add((percent > 50 && percent <= 75) ? entry : ghostEntry);
                perfectEntries.add((percent > 75) ? entry : ghostEntry);
            } else {
                badEntries.add(ghostEntry);
                mediumEntries.add(ghostEntry);
                goodEntries.add(ghostEntry);
                perfectEntries.add(ghostEntry);
            }
        }
        sets.add(getSet(badEntries, R.color.stats_bad));
        sets.add(getSet(mediumEntries, R.color.stats_medium));
        sets.add(getSet(goodEntries, R.color.stats_good));
        sets.add(getSet(perfectEntries, R.color.stats_perfect));
        return new LineData(sets);
    }
    private LineDataSet getSet(List<Entry> entries, int color) {
        LineDataSet set = new LineDataSet(entries, "");
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawHighlightIndicators(false);
        set.setValueFormatter(decimalFormatter);
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(15);
        int colorValue = ContextCompat.getColor(this, color);
        set.setFillColor(colorValue);
        set.setDrawCircles(false);
        set.setDrawFilled(true);
        set.setFillAlpha(230);
        set.setCubicIntensity(0.20f);
        set.setCircleHoleRadius(5);
        return set;
    }
    private void configureXAxis() {
        XAxis axis = chart.getXAxis();
        axis.setGranularity(1f);
        axis.setValueFormatter(formatter);
        axis.setDrawAxisLine(false);
        axis.setDrawGridLines(false);
        axis.setTextColor(Color.WHITE);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }
    private void configureYAxis(YAxis axis) {
        axis.setGranularity(1f);
        axis.setEnabled(false);
        axis.setDrawAxisLine(false);
        axis.setDrawTopYLabelEntry(false);
        axis.setDrawZeroLine(false);
        axis.setDrawLabels(false);
        axis.setDrawGridLines(false);
        axis.setAxisMinimum(0);
    }
    private void configureChart() {
        chart.setDescription(null);
        chart.setNoDataTextColor(Color.WHITE);
        chart.getLegend().setEnabled(false);
        chart.setExtraBottomOffset(15);
        chart.setXAxisRenderer(new CustomXAxisRenderer(chart.getViewPortHandler(), chart.getXAxis(), chart.getTransformer(YAxis.AxisDependency.LEFT)));
        configureXAxis();
        configureYAxis(chart.getAxisLeft());
        configureYAxis(chart.getAxisRight());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getCategory();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_detail);
        configureChart();
        refreshGUI();
        load();
        wrongAnswersList = (TPExpandableList<Quiz>) getSupportFragmentManager().findFragmentById(R.id.wrongAnswersLayout);
        if(isGlobal()) {
            wrongAnswersList.getView().setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().remove(wrongAnswersList).commit();
        }
        else {
            wrongAnswersList.setTitles(wrongAnswerListTitleFull, wrongAnswerListTitleEmpty);
            wrongAnswersList.setOnItemSelectListener(new QuizSheetCallback(wrongAnswersBottomSheet));
            wrongAnswersList.enableDivider(Color.WHITE);
        }
    }
    private void initDimens() {
        progressPercentageSize = getResources().getDimensionPixelSize(progressPercentageSizeRes);
        progressTextSize = getResources().getDimensionPixelSize(progressTextSizeRes);
    }
    private SpannableString getProgressString() {
        initDimens();
        String first = category.getProgress() + "%";
        String second = " " + category.getDescription(this);
        String output = first + second;
        SpannableString string = new SpannableString(output);
        string.setSpan(new AbsoluteSizeSpan(progressPercentageSize), 0, first.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        string.setSpan(new AbsoluteSizeSpan(progressTextSize), first.length(), output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }
    private void refreshGUI() {
        progressTextView.setBackgroundResource(category.getBackground());
        progressTextView.setText(getProgressString());
        hintTextView.setText(category.getHint(this));
        if(categoryDetail != null) {
            setDataToChart();
            if(!isGlobal()) setWrongAnswers(true);
        }
    }
    private void loadFailure() {
        Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        load();
                    }
                })
                .show();
    }
    private Boolean isGlobal() {
        return category.id == null;
    }
    private void load() {
        if(isGlobal()) loadGlobalCategory();
        else loadCategory();
    }
    private void loadGlobalCategory() {
        RetrofitManager.getHTTPStatsEndpoint().getGlobalCategory().enqueue(requestCallback);
    }
    private void loadCategory() {
        RetrofitManager.getHTTPStatsEndpoint().getCategory(category.id).enqueue(requestCallback);
    }
    private Callback<CategoryDetail> requestCallback = new Callback<CategoryDetail>() {
        @Override
        public void onResponse(Call<CategoryDetail> call, Response<CategoryDetail> response) {
            if(response.isSuccessful() && response.body().success) {
                categoryDetail = response.body();
                refreshGUI();
            } else {
                loadFailure();
            }
        }

        @Override
        public void onFailure(Call<CategoryDetail> call, Throwable t) {
            loadFailure();
        }
    };
    private void setWrongAnswers(Boolean needsLayoutParamsUpdate) {
        wrongAnswersList.setItems(categoryDetail.wrongAnswers, R.layout.gamedetails_quiz_view_holder, QuestionHolder.class, wrongAnswerHeight);
        wrongAnswersList.setListCounter(categoryDetail.wrongAnswers.size(), needsLayoutParamsUpdate);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(categoryDetail != null) setWrongAnswers(true);
    }

    @Override
    protected String getToolbarTitle(){
        return category.hint;
    }
    @Override
    protected int getSettingsVisibility(){
        return View.GONE;
    }
    @Override
    protected int getBackButtonVisibility(){
        return View.VISIBLE;
    }
    @Override
    protected int getHeartCounterVisibility() { return View.GONE; }

    public class CustomXAxisRenderer extends XAxisRenderer {
        public CustomXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
            super(viewPortHandler, xAxis, trans);
        }

        @Override
        protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
            String lines[] = formattedLabel.split("\n");
            for (int i = 0; i < lines.length; i++) {
                float vOffset = i * mAxisLabelPaint.getTextSize();
                Utils.drawXAxisValue(c, lines[i], x, y + vOffset, mAxisLabelPaint, anchor, angleDegrees);
            }
        }
    }
}
