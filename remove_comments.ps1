$javaFiles = Get-ChildItem -Path "d:\Uni senior year\AOOSE\Group 12 phase 3\JobRecruitmentSystem-\" -Recurse -Filter "*.java" -File | Where-Object { $_.FullName -notmatch "\\bin\\" }
$processedCount = 0
$removedCommentBlocks = 0

foreach ($file in $javaFiles) {
    try {
        $content = Get-Content $file.FullName -Raw -Encoding UTF8
        $originalContent = $content
        
        # Remove single-line comments that are standalone (but keep Javadoc /** */)
        $content = $content -replace "(?m)^\s*//\s*=+\s*$", ""  # Section separators
        $content = $content -replace "(?m)^\s*//\s*[-]+\s*$", ""  # Section separators with dashes
        
        # Count changes
        if ($content -ne $originalContent) {
            $processedCount++
            $diffLines = ($originalContent.Split("`n").Count - $content.Split("`n").Count)
            if ($diffLines -gt 0) {
                $removedCommentBlocks += $diffLines
            }
            Set-Content -Path $file.FullName -Value $content -Encoding UTF8 -NoNewline
        }
    }
    catch {
        Write-Host "Error processing $($file.Name): $_" -ForegroundColor Red
    }
}

Write-Host "`n=== Processing Complete ===" -ForegroundColor Green
Write-Host "Files processed: $processedCount" -ForegroundColor Cyan
Write-Host "Comment blocks removed: ~$removedCommentBlocks" -ForegroundColor Cyan
