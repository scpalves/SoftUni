<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>First Steps Into PHP</title>

</head>
<body>
<form>
    N: <input type="text" name="num" />
    <input type="submit" />
</form>
<?php
if(isset($_GET['num'])){
    $n = intval($_GET['num']);

    $a = 0;
    $b = 0;
    $c = 1;
    for($i=0;$i<$n;$i++){
        $temp1 = $a;
        $a = $b;
        $temp2 = $b;
        $b = $c;
        echo $c.' ';
        $c += $temp1+$temp2;
    }
}
?>
</body>
</html>
