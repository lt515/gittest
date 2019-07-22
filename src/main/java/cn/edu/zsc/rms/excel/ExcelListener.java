package cn.edu.zsc.rms.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hsj
 */
public class ExcelListener<T> extends AnalysisEventListener {

    private List<T> dataList = new ArrayList<>();

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Object object, AnalysisContext context) {
        dataList.add((T) object);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

    public List<T> getDataList() {
        return dataList;
    }

}
