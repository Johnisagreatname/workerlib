package yizhit.workerlib;

import yizhit.workerlib.entites.Archives;
import java.sql.SQLException;
import java.util.List;

public class Testtt {
    public static void main(String[] args) {
//        RoleModel roleModel= new RoleModel();
//        roleModel.setRoleName("工人");
//        try {
//            RoleModel roleId = roleModel.where("[roleName]=#{roleName}").first();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            String path = ApplicationConfig.getInstance().get("${entity.upload.workerlib.archives.photo.path}");
//            Archives entity = new Archives();
//            List<Archives> list = getArchives();
//            while (list.size() > 0) {
//                for (Archives item : list) {
//                    String filename = item.getIdNumber() + ".jpg";
//                    File file = new File("F:\\tianjian\\data\\faceImages\\" + filename);
//                    byte[] bytes = UploadUtils.getFileByteArray(file);
//                    String value = UploadUtils.upload(path, filename, bytes);
//                    item.setPhoto(value);
//                    item.where("id=#{id}").update("photo=#{photo}");
//                }
//
//                list = getArchives();
//            }
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        }
    }

    private static List<Archives> getArchives() throws SQLException {
        Archives entity = new Archives();
        return entity
                        .where("photo is null")
                        .query(0, 50);
    }
}
