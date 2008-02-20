// This code was taken from the online php manual at:
// http://us3.php.net/features.file-upload
<?php 
$uploaddir = './';
$uploadfile = $uploaddir . basename($_FILES['uploadFormElement']['name']);
echo '<pre>';
if (move_uploaded_file($_FILES['uploadFormElement']['tmp_name'], $uploadfile)) {
    echo "File is valid, and was successfully uploaded.\n";
} else {
    echo "Possible file uploadhttp://us3.php.net/features.file-upload attack!\n";
}

echo 'Here is some more debugging info:';
print_r($_FILES);
print "</pre>";
?>