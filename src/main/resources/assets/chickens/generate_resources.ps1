param(
    
    [switch] $All,

    [switch] $Textures,
        [switch] $Generate64xEntityTexture,

    [switch] $Templates,
        [switch] $GenerateBlockstatesTemplate,
        [switch] $GenerateModelsTemplate
)
if($All) {
    $Textures = $true;
    $Templates = $True;
}
if($Textures) {
    $Generate64xEntityTexture = $true;
}
if($Templates) {
    $GenerateBlockstatesTemplate = $true;
    $GenerateModelsTemplate = $true;
}

$location = Get-location

#generate 64x entity textures
if($Generate64xEntityTexture){
    Get-ChildItem -Path ($location.Path + "\textures\entity\") -Filter "*.png" | ForEach-Object {
        $oldLocation = $location.Path + "\textures\entity\";
        $old = $oldLocation + $_.Name;
        $new = $oldLocation + "64x\" + $_.Name;
        ffmpeg -i $old -vf "pad=64:64:0:0:0x00000000" -y $new;
    #    echo $old; echo $new;
    }
}

$chickenNameContent = (Get-Content -Path ($location.Path + "\chicken_list.txt")) -split "`r`n"
echo $chickenNameContent

#generate blockstates
if($GenerateBlockstatesTemplate) {
    $templateContent = Get-Content -Path ($location.Path + "\templates\blockstates\packed_galena_chicken.json")
#    echo $blockstatesTemplateContent
    ForEach($chickenName in $chickenNameContent) {
        $blockstatesJson = $templateContent -replace "galena_chicken", $chickenName;
        Set-Content -Path ($location.Path + "\blockstates\packed_" + $chickenName + ".json") $blockstatesJson;
    }
}

#generate model
if($GenerateModelsTemplate) {
    $templateContent = Get-Content -Path ($location.Path + "\templates\models\block\packed_galena_chicken.json")
#    echo $templateContent
    ForEach($chickenName in $chickenNameContent) {
        $modelsJson = $templateContent -replace "galena_chicken", $chickenName;
        Set-Content -Path ($location.Path + "\models\block\packed_" + $chickenName + ".json") $modelsJson;
    }
}