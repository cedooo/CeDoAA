# https://linux.cn/article-9495-1.html
find / -type f -print0 | xargs -0 du -h | sort -rh | head -n 10
#
# find / -type f -exec du -Sh {} + | sort -rh | head -n 10
# find / -type f -print0 | xargs -0 du | sort -n | tail -10 | cut -f2 | xargs -I{} du -sh {}
# find / -type f -ls | sort -k 7 -r -n | head -10 | column -t | awk '{print $7,$11}'
#