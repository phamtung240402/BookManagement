<?php 
include "connect.php";
$page = $_POST['page'];
$total = 5;
$pos = ($page - 1) * $total;
$loai = $_POST['TheLoai'];

$query = 'SELECT * FROM `sanpham` WHERE `TheLoai` = "'.$loai.'" LIMIT '.$page.', '.$total.'';
$data = mysqli_query($conn, $query);
$result = array();
while($row = mysqli_fetch_assoc($data)) {
    $result[] = ($row);
}

if(!empty($result)) {
    $arr = [
        'success' => true,
        'message' => 'thanhcong',
        'result' =>  $result 
    ]; 
} else {
    $arr = [
        'success' => false,
        'message' => 'khong thanh cong',
        'result' =>  $result 
    ];
}

echo json_encode($arr);
// print_r(json_encode($arr));
?>