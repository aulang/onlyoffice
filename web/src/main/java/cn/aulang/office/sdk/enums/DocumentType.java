package cn.aulang.office.sdk.enums;

/**
 * 文档类型
 *
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 14:31
 */
public enum DocumentType {
    text,
    spreadsheet,
    presentation;

    public static DocumentType fileType(String fileType) {
        switch (fileType) {
            case "xls":
            case "xlsx":
            case "ods":
            case "csv":
                return DocumentType.spreadsheet;
            case "ppt":
            case "pptx":
            case "odp":
                return DocumentType.presentation;
            case "doc":
            case "docx":
            case "odt":
            case "txt":
            default:
                return DocumentType.text;
        }
    }
}
