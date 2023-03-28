<?php 
include "connect.php";
$target_dir = "images/";
$query = "SELECT max(id) as id from sanpham";
$data = mysqli_query($conn, $query);
$result = array();
while($row = mysqli_fetch_assoc($data)) {
    $result[] = ($row); 
}

if($result[0]['id'] == null) {
    $name = 1;
} else {
    $name = ++$result[0]['id'];
}
$name = $name. ".jpg";
$target_file_name = $target_dir .$name;
if(isset($_FILES["file1"])) {
    if(move_uploaded_file($_FILES["file1"]["tmp_name"], $target_file_name)) {
        $arr = [
            'success' => true,
            'message' => 'thanhcong',
            'name' => $name
        ]; 
    } else {
        $arr = [
            'success' => false,
            'message' => 'khong thanh cong'
        ];
    }
}
    else {
        $arr = [
            'success' => false,
            'message' => 'loi anh',
        ];
    }
echo json_encode($arr);
// print_r(json_encode($arr));
?>