<?php 
include "connect.php";
$TenSanPham = $_POST['TenSanPham'];
$TheLoai = $_POST['TheLoai'];
$SoLuong = $_POST['SoLuong'];
$GioiThieu = $_POST['GioiThieu'];
$id = $_POST['id'];

$query = 'UPDATE `sanpham`  SET `TenSanPham` = "'.$TenSanPham.'", `TheLoai` = "'.$TheLoai.'", `SoLuong` = "'.$SoLuong.'", `GioiThieu` = "'.$GioiThieu.'" WHERE `id` = ' .$id;
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