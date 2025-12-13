# PowerShell script to remove unnecessary comments from remaining Java files
# Preserves Javadoc method comments (/** */) but removes other comment types

$workspaceRoot = "d:\Uni senior year\AOOSE\Group 12 phase 3\JobRecruitmentSystem-"
$javaFiles = Get-ChildItem -Path $workspaceRoot -Recurse -Filter "*.java" -File | Where-Object { $_.FullName -notmatch '\\bin\\' }

$processedCount = 0
$totalRemovedLines = 0
$filesModified = @()

Write-Host "=== Java Comment Cleanup Script ===" -ForegroundColor Cyan
Write-Host "Total files to process: $($javaFiles.Count)" -ForegroundColor Yellow
Write-Host ""

foreach ($file in $javaFiles) {
    try {
        $content = Get-Content $file.FullName -Raw -Encoding UTF8
        if ($null -eq $content) { continue }
        
        $originalContent = $content
        $originalLineCount = ($content -split "`n").Count
        
        # Remove section separator comments (// ====, // ----, etc.)
        $content = $content -replace '(?m)^\s*//\s*=+\s*$', ''
        $content = $content -replace '(?m)^\s*//\s*-+\s*$', ''
        $content = $content -replace '(?m)^\s*//\s*\*+\s*$', ''
        
        # Remove standalone single-line comments that are section labels
        $content = $content -replace '(?m)^\s*//\s*(Constructors?|Getters?|Setters?|Methods?|Fields?|Attributes?|Properties?)\s*$', ''
        $content = $content -replace '(?m)^\s*//\s*(CREATE|READ|UPDATE|DELETE|GET|SET|ADD|REMOVE)\s*(.*?)\s*$', ''
        $content = $content -replace '(?m)^\s*//\s*FUNCTION\s+\d+:.*$', ''
        
        # Remove TODO, FIXME, NOTE, XXX comments
        $content = $content -replace '(?m)^\s*//\s*(TODO|FIXME|NOTE|XXX|HACK|BUG).*$', ''
        
        # Remove inline comments that just restate the code
        $content = $content -replace '(?m)(.*?)\s+//\s*(Store|Set|Get|Create|Update|Delete|Insert|Find|Check|Validate|Hash|Verify|Compare|Add|Remove)\s+(the|a|an)\s+.*$', '$1'
        
        # Remove "Legacy", "For backward compatibility" comments
        $content = $content -replace '(?m)^\s*//\s*(Legacy|Backward compatibility|For backward compatibility).*$', ''
        
        # Remove empty comment blocks created by removal
        $content = $content -replace '(?m)^\s*//\s*$', ''
        
        # Remove multiple consecutive blank lines (created by comment removal)
        $content = $content -replace '(?m)(\r?\n\s*){3,}', "`n`n"
        
        # If content changed, save it
        if ($content -ne $originalContent) {
            Set-Content -Path $file.FullName -Value $content -Encoding UTF8 -NoNewline
            $newLineCount = ($content -split "`n").Count
            $removedLines = $originalLineCount - $newLineCount
            
            $processedCount++
            $totalRemovedLines += $removedLines
            $filesModified += $file.Name
            
            Write-Host "✓ $($file.Name) - Removed $removedLines lines" -ForegroundColor Green
        }
    }
    catch {
        Write-Host "✗ Error processing $($file.Name): $_" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "=== Processing Complete ===" -ForegroundColor Cyan
Write-Host "Files processed: $processedCount out of $($javaFiles.Count)" -ForegroundColor Green
Write-Host "Total comment lines removed: ~$totalRemovedLines" -ForegroundColor Green
Write-Host ""
Write-Host "Modified files:" -ForegroundColor Yellow
$filesModified | ForEach-Object { Write-Host "  - $_" }
