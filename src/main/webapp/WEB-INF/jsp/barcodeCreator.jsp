<!DOCTYPE html>
<html lang="en">
<head>
<style>
body
{
    background:url('https://s-media-cache-ak0.pinimg.com/originals/39/2d/67/392d675638b96f888aebf283910b3b50.jpg') center no-repeat;
}
</style>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <title>Barcode Generator</title>
</head>
<body>

<div class="container">
    <form method="get" action="submitBarcodeForm">
        <div class="form-group">
            <label class="pt-3"><h3><b>Text:</b></h3></label>
            <textarea class="form-control p-3" rows="3" placeholder="Write a text from which the bar code is to be generated, the bar code is generated for every text seperated by comma" name="input" required></textarea>
            <h3 class="pt-3"><b>Type of the barcode: </h3>     </p>
            <select class="form-control" name="barcodeType">
                <option value="128">128</option>
                <option value="QR">QR</option>
                <option value="Codabar">Codabar</option>
                <option value="EAN">EAN</option>
                <option value="Inter25">Inter25</option>
                <option value="Postnet">Postnet</option>
                <option value="39">39</option>
            </select>
<br>
        <button type="submit" class="btn-lg btn-primary">Generate barcodes</button>
        </div>
    </form>
</div>

</body>
</html>