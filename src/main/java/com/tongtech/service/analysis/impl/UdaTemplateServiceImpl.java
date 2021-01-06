package com.tongtech.service.analysis.impl;

import com.tongtech.common.vo.analysis.*;
import com.tongtech.dao.entity.analysis.*;
import com.tongtech.dao.repository.analysis.UdaTemplateRepository;
import com.tongtech.service.analysis.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tongtech.common.utils.Constant.*;


/**
 * <p>
 * 自定义分析模板表 服务实现类
 * </p>
 *
 * @author
 * @since 2020-06-29
 */
@Service
public class UdaTemplateServiceImpl implements IUdaTemplateService {

    private String timeDimLevelId,geoDimLevelId,themeId,queryFieldString,tableName,sqlStrings;
    private HashMap<String,Object> resultMap;
    private boolean busyHoursFlag;
    private UdaTheme themeobj;
    private DataVo dataVo ;


    @Autowired
    IUdaThemeService udaThemeService;

    @Autowired
    IUdaThemeDimService udaThemeDimService;

    @Autowired
    IUdaThemeDimLevelService udaThemeDimLevelService;

    @Autowired
    IUdaThemeMeasureService udaThemeMeasureService;

    @Autowired
    IUdaCfgDimMapService udaCfgDimMapService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    UdaTemplateRepository udaTemplateRepository;

    public void init(TemplateVo tplVo){
        resultMap = new HashMap<>();
        dataVo = new DataVo();
        themeobj = udaThemeService.findUdaThemeByTemplateVo(tplVo);
        busyHoursFlag = UDA_THEME_NOTE_VALID_FLAG.equals(themeobj.getNotes());
        queryFieldString = getQueryFieldString(tplVo);
        if( busyHoursFlag){
            UdaCfgDimMap udaCfgDimMap = udaCfgDimMapService.findUdaCfgDimMap(themeId,timeDimLevelId,geoDimLevelId);
            tableName = udaCfgDimMap.getTableName();
        }else {
            // tableName = getTableName(tplVo);
            tableName = TABLE_NAME;
        }
    }

    public String getQueryFieldString(TemplateVo tplVo){
        StringBuilder queryStringBuilder=new StringBuilder();
        themeId = tplVo.getThemeId();
        List<LevelVo>  lists= tplVo.getShowLevelList();

        for (LevelVo levelVo : lists) {
            UdaThemeDim dimObj = udaThemeDimService.findUdaThemeDimByLevelVo(levelVo ,themeId);
            UdaThemeDimLevel udaThemeDimLevel = udaThemeDimLevelService.findUdaThemeDimByLevelVo(levelVo,themeId);
            if (DIM_TIME.equals(dimObj.getDimName())){
                if(busyHoursFlag) {
                    dataVo.getColumns().add("时间");
                    queryStringBuilder.append("time_id").append(",");
                } else {
                    dataVo.getColumns().add("派单时间");
                    queryStringBuilder.append("assign_time").append(",");
                }
                timeDimLevelId = levelVo.getLevelId();
            }else {
                dataVo.getColumns().add(levelVo.getLevelName());
                if(busyHoursFlag){
                    queryStringBuilder.append(udaThemeDimLevel.getFieldName()).append(",");
                }else{
                    queryStringBuilder.append(udaThemeDimLevel.getFieldName()).append(",");
                }

                if(DIM_GEO.equals(dimObj.getDimName())) {
                    geoDimLevelId = levelVo.getLevelId();
                }
            }
        }
        for(MeasureVo mvo : tplVo.getMeasureList()) {
            UdaThemeMeasure udaThemeMeasure = udaThemeMeasureService.findUdaThemeMeasureByMeasureVo(mvo,themeId);
            if (udaThemeMeasure==null){
                continue;
            }

            if(busyHoursFlag){
                queryStringBuilder.append(udaThemeMeasure.getFieldName()).append(",");
            }else {
                queryStringBuilder.append(udaThemeMeasure.getFieldName()).append(",");
            }

            dataVo.getColumns().add(mvo.getMeasureName());
        }
        queryStringBuilder.replace(queryStringBuilder.lastIndexOf(","),queryStringBuilder.length(),"");
        String queryFieldString = queryStringBuilder.toString();
        return queryFieldString;

    }


    public String getTableName(TemplateVo tplVo){
        boolean freeOrBusyFlag = false;
        boolean isStation = false;
        boolean  isHour = false;
        String tableName = null;
        List<LevelVo> levelVos = new ArrayList<>();
        levelVos.addAll(tplVo.getShowLevelList());
        levelVos.addAll(tplVo.getFilterLevelList());
        for(LevelVo levelVo: levelVos) {
            if (freeOrBusyFlag && isStation && isHour) {
                break;
            }
            //首先判断是不是忙时
            if (levelVo.getLevelName().equals("忙时")) {
                freeOrBusyFlag = true;
            }
            //判断是否是基站和站址
            if (levelVo.getLevelName().equals("基站") || levelVo.getLevelName().equals("站址")) {
                isStation = true;
            }
            //判断是否是小时
            if (levelVo.getLevelName().equals("时")) {
                isHour = true;
            }
        }

        //根据以上判断的结果，来确定最终的表名称
        if (!isStation){
            if (freeOrBusyFlag){
                tableName="pm_lte_cel_bh_cel";
            }else {
                if (isHour){
                    tableName="pm_lte_cel_h_cel";
                }  else {
                    tableName="pm_lte_cel_oh_cel";
                }
            }

        }else {
            if (freeOrBusyFlag){
                tableName="pm_lte_enb_bh_cel";
            }else {
                if (isHour){
                    tableName="pm_lte_enb_h_cel";
                }  else {
                    tableName="pm_lte_enb_oh_cel";
                }
            }
        }
        return tableName;
    }

    @Override
    public Map<String, Object> queryDataByTemplate(TemplateVo tplVo, Integer pageSize, Integer pageNum) {
        init(tplVo);
        List<String>  eachFilterStringPart = buildFiltersList(tplVo);
        String filterForResult = buildFilter(tplVo,eachFilterStringPart);
        buildSqlString(filterForResult,pageSize,pageNum);
        return executeQuery(sqlStrings,pageSize,pageNum);
    }

    public List<String> buildFiltersList(TemplateVo tplVo){
        List<LevelVo> filterLevelList = tplVo.getFilterLevelList();
        List<String> eachFilterStringPart=new ArrayList<>();
        for (LevelVo levelVo : filterLevelList) {
            UdaThemeDim dimObj = udaThemeDimService.findUdaThemeDimByLevelVo(levelVo,themeId);
            if (dimObj == null){
                continue;
            }
            if (DIM_TIME.equals(dimObj.getDimName())){
                //如果是时间维，就需要进行日期格式的转换
                List<SingleVo> valList = levelVo.getValList();
                if (valList.size()==0){
                    continue;
                }
                String endTimeString = null;
                String timeString = valList.get(0).getId();
                //根据具体的时间字段和选择的类型，判断出两个时间的范围
                if(valList.size() ==2){
                    endTimeString = valList.get(1).getId();
                    System.out.println(valList.get(1).getId());
                    System.out.println(valList.get(1).getValue());
                }
                System.out.println(valList.get(0).getId());
                System.out.println(valList.get(0).getValue());
                System.out.println(timeString);
                System.out.println(endTimeString);
                String timeJudgeString=getTimeJudgeString(levelVo,timeString,endTimeString);
                if (timeJudgeString!=null){
                    eachFilterStringPart.add(timeJudgeString);
                }
            }else {
                //进行简单的字符串等于的拼接
                UdaThemeDimLevel one = udaThemeDimLevelService.findUdaThemeDimByLevelVo(levelVo,themeId);
                List<SingleVo> valList = levelVo.getValList();
                String oneFilter="";
                if (valList.size()>0){
                    oneFilter+="(";
                    for (int i = 0; i < valList.size(); i++) {
                        oneFilter =oneFilter + "'"+valList.get(i).getValue()+"'";
                        if (i!=valList.size()-1){
                            oneFilter+=", ";
                        }
                    }
                    oneFilter+=")";
                }
                if (!EMPTY_STRING.equals(oneFilter)){
                    String fieldName = one.getFieldName();
                    if(this.busyHoursFlag) {
                        eachFilterStringPart.add(fieldName + " in " + oneFilter);
                    }else {
                        eachFilterStringPart.add( fieldName + " in " + oneFilter );
                    }
                }
            }
        }
        //转换conditons为过滤条件  "[LTE掉线率2] > 2|AND|[LTE掉线率1] > 5  先替换两个方括号中的内容，然后再替换掉两个竖线就成为一个最终的拼接条件
        String conditions = tplVo.getConditions();
        String changedFilterString=getConditionField(conditions,themeId);
        if (changedFilterString != null){
            eachFilterStringPart.add(changedFilterString);
        }
        return eachFilterStringPart;

    }

    public String buildFilter(TemplateVo tplVo,List<String> eachFilterStringPart){
        //将所有的过滤条件拼接成一个过滤条件
        StringBuilder builderForFilter=new StringBuilder();
        for (int i = 0; i < eachFilterStringPart.size(); i++) {
            builderForFilter.append(eachFilterStringPart.get(i));
            builderForFilter.append(" ");
            if (i != eachFilterStringPart.size()-1){
                builderForFilter.append(" and ");
            }
        }
        String filterForResult = builderForFilter.toString();
        return filterForResult;
    }

    public String buildSqlString(String filterForResult,Integer pageSize, Integer pageNum){

        String sqlStringForCounts = null;
        if(!this.busyHoursFlag) {
            sqlStrings = "select "+queryFieldString+ " from "+ tableName;
            sqlStringForCounts = "select count(*) from "+ tableName;
        }
        else {
            sqlStrings="select "+queryFieldString+ " from "+ tableName +"  a left join dim_ne_lte_ec b on a.int_id = b.ec_id ";
            sqlStringForCounts="select count(int_id) from "+ tableName + " a left join dim_ne_lte_ec b on a.int_id = b.ec_id ";
        }

        if (!EMPTY_STRING.equals(filterForResult.trim())){
            sqlStrings+= "  where "+filterForResult;
            sqlStringForCounts +=  "  where "+filterForResult;
        }
        Integer  totalCounts = jdbcTemplate.queryForObject(sqlStringForCounts, Integer.class);
        if (pageSize != null && pageNum != null) {
            resultMap.put("totalNum",totalCounts);
            resultMap.put("pageNum",pageNum);
            resultMap.put("pageSize",pageSize);
            Integer startIndex = (pageNum - 1) * pageSize, endIndex = pageNum * pageSize;
            sqlStrings += " limit  " + startIndex + " , " + endIndex;
        } else {
            resultMap.put("totalNum","");
            resultMap.put("pageNum","");
            resultMap.put("pageSize","");
            if (totalCounts > MAX_QUERY_COUNT) {
                sqlStrings += "limit  0, 1000000";
            }
        }
        System.out.println(sqlStrings);
        return sqlStrings;
    }

    public HashMap<String,Object> executeQuery(String sqlStrings,Integer pageSize, Integer pageNum){
        HashMap<String,Object> resultMap=new HashMap<>();
        System.out.println(sqlStrings);
        List<Object> rows = jdbcTemplate.query(sqlStrings, new RowMapper<Object>() {
            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                int columnCount = resultSet.getMetaData().getColumnCount();
                LinkedList<Object> results = new LinkedList<>();
                for (int i1 = 0; i1 < columnCount; i1++) {
                    results.add(resultSet.getObject(i1 + 1));
                }
                return results;
            }
        });
        for(Object row:rows) {
            System.out.println(row);
            Row obj = new Row();
            obj.setValues((List)row);
            dataVo.getRows().add(obj);
        }
        resultMap.put("data",dataVo);
        return resultMap;
    }

    public String getConditionField(String conditions,String themeId) {
        String fieldName = null;
        if (EMPTY_STRING.equals(conditions)) {
            return conditions = null;
        }
        String reg = "(\\[.*?\\])";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(conditions);

        while (matcher.find()) {
            for(int i=0 ; i <matcher.groupCount(); i ++) {
                fieldName = matcher.group(i).substring(1,matcher.group(i).length()-1);
                UdaThemeMeasure udaThemeMeasure = udaThemeMeasureService.findUdaThemeMeasureByFiledName(fieldName,themeId);
                if(udaThemeMeasure == null) {
                    return null;
                }
                conditions = conditions.replace(fieldName,udaThemeMeasure.getFieldName());
            }
            conditions =conditions.replace("[","").replace("]","");
            conditions = conditions.replace("|"," ");
        }
        return conditions;
    }

    private String getTimeJudgeString(LevelVo levelVo, String timeString, String endDateString) {
        String levelName = levelVo.getLevelName();
        if (levelName == null || EMPTY_STRING .equals(levelName)){
            return null;
        }
        boolean intervalFlag = false;
        if(endDateString != null){
            intervalFlag = true;
        }
        switch (levelName){
            case "小时":
                return getHourString(levelVo,timeString,intervalFlag,endDateString);
            case "日":
                return getDayString(levelVo,timeString,intervalFlag,endDateString);
            case "周":
                return getWeekString(levelVo,timeString,intervalFlag,endDateString);
            case "月":
                return getMonthString(levelVo,timeString,intervalFlag,endDateString);
            case "季度":
                return getSeasonString(levelVo,timeString,intervalFlag,endDateString);
        }
        return null;
    }

    private String getHourString(LevelVo levelVo, String timeString,boolean intervalFlag,String endDateString){
        //20200703170000
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date startTime = sdf.parse(timeString);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTimeString = simpleDateFormat.format(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            calendar.add(Calendar.HOUR_OF_DAY,1);
            Date endTime = calendar.getTime();
            String endTimeString = simpleDateFormat.format(endTime);
            if(intervalFlag){
                endTime = sdf.parse(endDateString);
                calendar.setTime(endTime);
                calendar.add(Calendar.HOUR_OF_DAY,1);
                endTime = calendar.getTime();
                endTimeString = simpleDateFormat.format(endTime);
            }
            String returnString=" assign_time >= '"+startTimeString+"' and assign_time < '"+endTimeString+"'";
            return returnString;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getDayString(LevelVo levelVo, String timeString,boolean intervalFlag,String endDateString) {
        //20200703
        String returnString;
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMMdd");
        try {
            Date startTime = sdf1.parse(timeString);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTimeString = simpleDateFormat.format(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            Date endTime = calendar.getTime();
            String endTimeString = simpleDateFormat.format(endTime);
            if(intervalFlag){
                endTime = sdf1.parse(endDateString);
                calendar.setTime(endTime);
                calendar.add(Calendar.DAY_OF_MONTH,1);
                endTime = calendar.getTime();
                endTimeString = simpleDateFormat.format(endTime);
            }
            if (this.busyHoursFlag) {
                if(intervalFlag){
                    returnString = " time_id >= " + timeString + " and time_id <= " + endDateString;
                }else {
                    returnString = " time_id = " + timeString;
                }
            } else {
                //returnString=" scan_start_time >= '"+startTimeString+"' and scan_stop_time < '"+endTimeString+"'";
                returnString=" assign_time >= '"+startTimeString+"' and assign_time < '"+endTimeString+"'";
            }
            return returnString;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getWeekString(LevelVo levelVo, String timeString,boolean intervalFlag,String endDateString) {
        String returnString = null;
        String substring = timeString.substring(4);
        int weekNum = Integer.parseInt(substring);
        String yearStrings = timeString.substring(0, 4);
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy");
        try {
            Date startTime = sdf2.parse(yearStrings);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            calendar.set(Calendar.WEEK_OF_YEAR,weekNum);
            calendar.set(Calendar.DAY_OF_WEEK,1);
            startTime = calendar.getTime();
            calendar.set(Calendar.DAY_OF_WEEK,7);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            Date endTime = calendar.getTime();
            if(intervalFlag){
                substring = endDateString.substring(4);
                weekNum = Integer.parseInt(substring);
                yearStrings = endDateString.substring(0, 4);
                startTime = sdf2.parse(yearStrings);
                calendar.setTime(startTime);
                calendar.set(Calendar.WEEK_OF_YEAR,weekNum);
                calendar.set(Calendar.DAY_OF_WEEK,7);
                calendar.add(Calendar.DAY_OF_MONTH,1);
                endTime = calendar.getTime();
            }
            String startTimeString = simpleDateFormat.format(startTime);
            String endTimeString = simpleDateFormat.format(endTime);
            if(this.busyHoursFlag) {
                if(intervalFlag){
                    returnString = " time_id >= " + timeString + " and time_id <= " + endDateString;
                }else{
                    returnString=" time_id = " + timeString;
                }
            } else {
                // returnString=" scan_start_time >= '"+startTimeString+"' and scan_stop_time < '"+endTimeString+"'";
                returnString=" assign_time >= '"+startTimeString+"' and assign_time < '"+endTimeString+"'";
            }
            return returnString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getMonthString(LevelVo levelVo, String timeString,boolean intervalFlag,String endDateString) {
        String returnString = null;
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMM");
        try {
            Date startTime = sdf3.parse(timeString);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTimeString = simpleDateFormat.format(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            calendar.add(Calendar.MONTH, 1);
            Date endTime = calendar.getTime();
            String endTimeString = simpleDateFormat.format(endTime);
            if( intervalFlag ){
                startTime = sdf3.parse(endDateString);
                calendar.setTime(startTime);
                calendar.add(Calendar.MONTH,1);
                endTime = calendar.getTime();
                endTimeString = simpleDateFormat.format(endTime);
            }
            if (this.busyHoursFlag) {
                if ( intervalFlag ) {
                    returnString = " time_id >= " + timeString + " and time_id <= " + endDateString;
                } else {
                    returnString = " time_id = " + timeString;
                }
            } else {
                // returnString=" scan_start_time >= '"+startTimeString+"' and scan_stop_time < '"+endTimeString+"'";
                returnString=" assign_time >= '"+startTimeString+"' and assign_time < '"+endTimeString+"'";
            }
            return returnString;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getSeasonString(LevelVo levelVo, String timeString,boolean intervalFlag,String endDateString) {
        //2020Q1
        String returnString = null;
        String q = timeString.substring(timeString.indexOf("Q") + 1);
        String yearString = timeString.substring(0, 4);
        String startTimeString = "";
        String endTimeString = "";
        int quarterNum = Integer.parseInt(q);
        if (quarterNum == 1) {
            startTimeString = yearString + "0101";
        } else if (quarterNum == 2) {
            startTimeString = yearString + "0401";
        } else if (quarterNum == 3) {
            startTimeString = yearString + "0701";
        } else {
            startTimeString = yearString + "1001";
        }
        if (intervalFlag) {
            q = endDateString.substring(5);
            yearString = timeString.substring(0, 4);
            switch (q) {
                case "1":
                    endTimeString = yearString + "0101";
                    break;
                case "2":
                    endTimeString = yearString + "0401";
                    break;
                case "3":
                    endTimeString = yearString + "0701";
                    break;
                case "4":
                    endTimeString = yearString + "1001";
                    break;
                default:
                    break;
            }
        }
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMdd");
        try {
            Date startTime = sdf4.parse(startTimeString);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTimeString1 = simpleDateFormat.format(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            calendar.add(Calendar.MONTH, 3);
            Date endTime = calendar.getTime();
            String endTimeString1 = simpleDateFormat.format(endTime);
            if ( intervalFlag) {
                endTime = sdf4.parse(endTimeString);
                calendar.setTime(endTime);
                calendar.add(Calendar.MONTH,3);
                endTime = calendar.getTime();
                endTimeString1 = simpleDateFormat.format(endTime);
            }
            if (busyHoursFlag) {
                if( intervalFlag ) {
                    returnString = " time_id >=" + timeString.replace("Q","0") + " and time_id <= " + endDateString.replace("Q","0");
                } else {
                    returnString = " time_id = " + timeString.replace("Q", "0");
                }
            } else {
                // returnString="scan_start_time >= '"+startTimeString1+"' and scan_stop_time < '"+endTimeString1+"'";
                returnString=" assign_time >= '"+startTimeString+"' and assign_time < '"+endTimeString+"'";
            }
            return returnString;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResponseEntity<Resource> exportDataForTemplate(TemplateVo model) {

        Map<String, Object> dataMap = null;
        try {
            dataMap = queryDataByTemplate(model, null, null);
            HSSFWorkbook workbook = buildExcel((DataVo) dataMap.get("data"));
            return buildDownloadInfo(workbook);
        } catch (Exception e) {
            // myloger.warn("查询错误：" + e.getMessage());
            System.out.println("查询错误：" + e.getMessage());
            return null;
        }
    }


    private HSSFWorkbook buildExcel(DataVo  data){
        HSSFWorkbook workbook = new HSSFWorkbook();
        List<Row> rows = data.getRows();
        //将行和列用于生成excel表格
        //设置表头，首先取出设置表头所需要的数据
        //根据表头数据，生成表单当中的表头
        if (rows != null && rows.size() > 0) {
            HSSFSheet sheet = null;
            List<String> columns = data.getColumns();
            String nameStringPart = SHEET_NAME_PREFIX;
            int sheetNum = rows.size()%MAX_SHEET_ROW ==0 ? rows.size()%MAX_SHEET_ROW: rows.size()/MAX_SHEET_ROW+1;
            for(int i= 1;i<= sheetNum; i++){
                String nameString = nameStringPart + i;
                sheet = workbook.createSheet(nameString);
                setTitle(workbook,sheet,columns);
            }
            int currentSheetNum = 0;
            int rowNum = 1;
            HSSFRow hssfRow = null;
            for(Row row: rows){
                sheet = workbook.getSheetAt(currentSheetNum);
                hssfRow = sheet.createRow(rowNum++);
                List values= row.getValues();
                for(int n=0; n<values.size(); n++){
                    hssfRow.createCell(n).setCellValue(values.get(n) ==null ? "":values.get(n).toString());
                }
                if(rowNum > MAX_SHEET_ROW){
                    currentSheetNum++;
                    rowNum =1;
                }
            }
        }
        return workbook;

    }

    private ResponseEntity<Resource> buildDownloadInfo(HSSFWorkbook workbook)
    {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            workbook.write(bos);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("charset", "utf-8");
            String filename = "data-for-model-query.xlsx";
            filename = URLEncoder.encode(filename, "UTF-8");
            headers.add("Content-Disposition", "attachment;filename=" + filename);
            Resource resource = new InputStreamResource(new ByteArrayInputStream(bos.toByteArray()));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/x-msdownload"))
                    .body(resource);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(bos);
        }
    }



    private void setTitle(HSSFWorkbook workbook, HSSFSheet sheet, List<String> titles) {
        HSSFRow row = sheet.createRow(0);
        //  设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        for (int i = 0; i < titles.size(); i++) {
            sheet.setColumnWidth(i,10*256);
        }
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        //进行标题的设置
        HSSFCell cell=null;
        for (int i = 0; i < titles.size(); i++) {
            cell=row.createCell(i);
            cell.setCellValue(titles.get(i));
            cell.setCellStyle(style);
        }
    }

    @Override
    public void save(UdaTemplate udaTemplate) {
        udaTemplateRepository.save(udaTemplate);
    }

    @Override
    public List<UdaTemplate> findByThemeId(int themId) {
       return udaTemplateRepository.findByThemeId(themId);
    }
}