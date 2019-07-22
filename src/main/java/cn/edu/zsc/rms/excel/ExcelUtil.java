package cn.edu.zsc.rms.excel;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


/**
 * @author hsj
 */
public class ExcelUtil {

    public static void excelExport(OutputStream out,
                                   Class<? extends BaseRowModel> excelClass,
                                   List<? extends BaseRowModel> context) {
        try {
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            Sheet sheet1 = new Sheet(1, 0, excelClass);
            writer.write(context, sheet1);
            writer.finish();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T extends BaseRowModel> List<T> excelImport(InputStream in, Class<T> excelClass, int headLineMun) {
        try {
            ExcelListener<T> listener = new ExcelListener<>();
            ExcelReader excelReader = new ExcelReader(in, null, listener);
            excelReader.read(new Sheet(1, headLineMun, excelClass));
            return listener.getDataList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
