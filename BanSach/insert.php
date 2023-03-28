<?php 
include "connect.php";
$TenSanPham = $_POST['TenSanPham'];
$TheLoai = $_POST['TheLoai'];
$SoLuong = $_POST['SoLuong'];
$GioiThieu = $_POST['GioiThieu'];
$Anh = $_POST['Anh'];

$query = 'INSERT INTO `sanpham` (`TenSanPham`, `TheLoai`, `SoLuong`, `GioiThieu`, `Anh`) 
VALUES ("'.$TenSanPham.'","'.$TheLoai.'", '.$SoLuong.', "'.$GioiThieu.'", "'.$Anh.'")';
$data = mysqli_query($conn, $query);
$result = array();
while($row = mysqli_fetch_assoc($data)) {
    $result[] = ($row);
}

if(!empty($result)) {
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