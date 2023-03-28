<?php 
include "connect.php";
$id = $_POST['id'];
$query = 'DELETE FROM `sanpham` WHERE `id` =' .$id;
$data = mysqli_query($conn, $query);
if($data == true) {
    $arr = [
        'success' => true,
        'message' => 'thanhcong',
    ]; 
} else {
    $arr = [
        'success' => false,
        'message' => 'khong thanh cong',
    ];
}

echo json_encode($arr);
// print_r(json_encode($arr));
?>