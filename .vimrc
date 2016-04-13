set expandtab
set shiftwidth=2
set softtabstop=2

" Make vim more useful
set nocompatible

" Unix line endings
set ff=unix

" Change mapleader
let mapleader = ","

if has("gui_running")
    set anti
    set gfn=Menlo:h10
    " set guifont=Ubuntu_Mono:h14
    set fuoptions=maxvert
    set guioptions-=T
    set linespace=4
endif

" Pathogen
execute pathogen#infect()
" generate helptags for everything in âruntimepathâ
call pathogen#helptags()
filetype plugin indent on

" Turn off swap files
set noswapfile


" set color scheme
set t_Co=256
syntax on
set background=dark
colorscheme hybrid

" enable mouse
set mouse=a

" Create window splits easier. The default
" way is Ctrl-w,v and Ctrl-w,s. Remapped to
" vv and ss
nnoremap <silent> vv <C-w>v
nnoremap <silent> ss <C-w>s

" Tabs
set smartindent
set tabstop=2
set shiftwidth=2
set expandtab
set smarttab

" Linespacing
set linespace=1 

" No error audo
set visualbell
set noerrorbells visualbell t_vb=

" MULTIPURPOSE TAB KEY
" Indent if we're at the beginning of a line. Else,
" Do completion 
function! InsertTabWrapper()
    let col = col('.') - 1
    if !col || getline('.')[col - 1] !~ '\k'
        return "\<tab>"
    else
        return "\<c-p>"
    endif
endfunction
inoremap <tab> <c-r>=InsertTabWrapper()<cr>
inoremap <s-tab> <c-n>

" No Grasshopper
map <Left> :echo "no!"<cr>
map <Right> :echo "no!"<cr>
map <Up> :echo "no!"<cr>
map <Down> :echo "no!"<cr>

" show line numbers
set number
set cursorline

" Search
set ignorecase    " ignore case when searching
set smartcase     " ignore case if search pattern is all lowercase, case-sensitive otherwise
set hlsearch      " highlight search terms
set incsearch     " show search matches as you type

" autoindenting
set autoindent

" Move around splits with <c-hjkl>
nnoremap <c-j> <c-w>j
nnoremap <c-k> <c-w>k
nnoremap <c-h> <c-w>h
nnoremap <c-l> <c-w>l

" Syntax folding
set foldmethod=indent
set foldlevel=20
" set foldnestmax=2
nnoremap <space> za
vnoremap <space> zf

"NERDTree toggle shortcut
nmap <leader>ne :NERDTreeToggle<cr>

" JSHint (:q to close)
" jshint validation
nnoremap <silent><F1> :JSHint<CR>
inoremap <silent><F1> <C-O>:JSHint<CR>
vnoremap <silent><F1> :JSHint<CR>

" show next jshint error
nnoremap <silent><F2> :lnext<CR>
inoremap <silent><F2> <C-O>:lnext<CR>
vnoremap <silent><F2> :lnext<CR>

" show previous jshint error
nnoremap <silent><F3> :lprevious<CR>
inoremap <silent><F3> <C-O>:lprevious<CR>
vnoremap <silent><F3> :lprevious<CR>
 
" Run current file in ruby
imap <Leader>rr <ESC>:!ruby %<CR>
nmap <Leader>rr :!ruby %<CR>

 " go to tab by number
 noremap <leader>1 1gt
 noremap <leader>2 2gt
 noremap <leader>3 3gt
 noremap <leader>4 4gt
 noremap <leader>5 5gt
 noremap <leader>6 6gt
 noremap <leader>7 7gt
 noremap <leader>8 8gt
 noremap <leader>9 9gt
 noremap <leader>0 :tablast<cr>

