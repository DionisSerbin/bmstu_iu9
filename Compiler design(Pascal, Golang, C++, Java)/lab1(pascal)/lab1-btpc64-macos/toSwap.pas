program toSwap;
const
  arrayLength = 10;
var
  inputArray : array [1..arrayLength] of integer;
  i, j, tempValue: integer;
begin
  writeln ('Исходный массив: ');
  {заполнение массива случайными числами}
  for i := 1 .. arrayLength do
  begin
    inputArray[i] := i;
    write (inputArray[i]:4);
  end;
  writeln;
end. 