# FlowLayout
 流式布局－FlowLayout
 
 属性       | 含义           | 类型   |
--------------------|------------------|-----------------------|
horizontalMargin | 设置view之间的水平间距   | dimension   |
verticalMargin       | 设置view之间的垂直间距   | dimension   |

##  使用
 定义 adapter

```
public class Adapter extends FlowLayout.BaseAdapter {
    ArrayList<String> strings;
    Context context;
    public Adapter(  ArrayList<String> strings,Context context) {
        super();
        this.strings = strings;
        this.context = context;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public View getItemView(int index, FlowLayout parent) {
        TextView textView = new TextView(context);
        textView.setText(strings.get(index));
        return textView;
    }
}

```
设置数据和监听

```
        flow = (FlowLayout) findViewById(R.id.flow);
        strings = new ArrayList<>();
        strings.add("11111");
        strings.add("222");
        strings.add("333");
        strings.add("44444");
        flow.setAdapter(new Adapter(strings, this));
        flow.setOnItemClickListener(new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, FlowLayout flowLayout, int position) {
                Toast.makeText(MainActivity.this, strings.get(position), Toast.LENGTH_LONG).show();
            }
        });```