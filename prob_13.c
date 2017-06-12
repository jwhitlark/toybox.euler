#include <stdio.h>

// Make funcs to store #'s properly in arrays.  test

int merge_num_in_array(int target[], int num, int i)
{
  while ( num > 0 )
    {

    }
}


int bump(int cry, int i, char result[])
{
  if ( cry > 10 )
    NULL;
  return 0;
}

int main()
{
  char result[100];

  char a[] = "37107287533902102798797998220837590246510135740250";
  char b[] = "37107287533902102798797998220837590246510135740251";

  for (int i = 0; i < 50; i++)
    {
      int carry = a[i] + b[i];
      // Next number needs to be bumped, too
      if ( carry > 10 )
	{
	  bump(carry, i, result);
	  result[i] = carry - 10;
	}
      else if (carry > 10 )
	{
	  result[i+1] += 1;
	  result[i] = carry - 10;
	}
      else
	result[i] = carry;

    }

  printf("test%s", a);
  return 0;
}
