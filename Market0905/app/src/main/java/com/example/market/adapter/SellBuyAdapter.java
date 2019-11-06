package com.example.market.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.ModifyActivity;
import com.example.market.R;
import com.example.market.ResultActivity;
import com.example.market.constants.Constants;
import com.example.market.sub.ReviewWriteActivity;
import com.example.market.response.HideOffResponse;
import com.example.market.sub.SellCompleteActivity;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class SellBuyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Main> items = new ArrayList<Main>();
    int button_code;
    Activity activity;
    AsyncHttpClient client;
    HideOffResponse hideOffResponse;
    FragmentTransaction tran;
    FragmentManager fm;
    int num;
    Animation translateUpAnim;
    Animation translateStaylAnim;
    boolean isPageOpen;
    int position;
    Main modify, review, result;
    String user_name;   // 로그인한 사람의 이름
    LinearLayout linearLayout_menu_sale;

    public SellBuyAdapter(Activity activity) {
        this.activity = activity;
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        ImageView imageView_image0, imageView_image1, imageView_image2;
        TextView textView_user_name, textView_subject, textView_category_code,
                textView_area, textView_price, textView_content, textView_reply_count,
                textView_interest_count, textView_readcount, textview_lat,
                textview_lng, textview_board_date ;
        LinearLayout linearLayout_sale, linearLayout_menu_sale, linearLayout_list;
        Button button_sellComplete, button_reviewCompleteSell,
                button_hideOff, button_review, button_reviewCompleteBuy, button_context_menu;

        ImageView imageView_goods_image;

        // 컨텍스트 버튼
        Button button_renewal, button_sale, button_modify, button_hide, button_delete,
                button_review2, button_not_trade;
        ImageLoader imageLoader;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            // 컨텍스트 메뉴를 위한 설정
//            itemView.setOnCreateContextMenuListener(this);
            button_renewal = itemView.findViewById(R.id.button_renewal);
            button_sale = itemView.findViewById(R.id.button_sale);
            button_modify = itemView.findViewById(R.id.button_modify);
            button_hide = itemView.findViewById(R.id.button_hide);
            button_delete = itemView.findViewById(R.id.button_delete);
            button_review2 = itemView.findViewById(R.id.button_review2);
            button_not_trade = itemView.findViewById(R.id.button_not_trade);
            button_context_menu = itemView.findViewById(R.id.button_context_menu);

            client = new AsyncHttpClient();
            hideOffResponse = new HideOffResponse(activity);
            fm = activity.getFragmentManager();
            tran = fm.beginTransaction();
            isPageOpen = false;

            translateUpAnim = AnimationUtils.loadAnimation(activity, R.anim.up);
            translateStaylAnim = AnimationUtils.loadAnimation(activity, R.anim.stay);

            imageView_image0 = itemView.findViewById(R.id.imageView_goods_image);
            imageView_image1 = itemView.findViewById(R.id.imageView_goods_image);
            imageView_image2 = itemView.findViewById(R.id.imageView_goods_image);
            textView_subject = itemView.findViewById(R.id.textView_subject);

            textView_area = itemView.findViewById(R.id.textView_area);
            textview_board_date = itemView.findViewById(R.id.texteview_board_date);
            textView_price = itemView.findViewById(R.id.textView_price);
            textView_reply_count = itemView.findViewById(R.id.textView_reply_count);
            textView_interest_count = itemView.findViewById(R.id.textView_interest_count);

            linearLayout_sale = itemView.findViewById(R.id.linearLayout_sale);
            linearLayout_menu_sale = itemView.findViewById(R.id.linearLayout_menu_sale);
            linearLayout_list = itemView.findViewById(R.id.linearLayout_list);
            button_sellComplete = itemView.findViewById(R.id.button_sellComplete);
            button_reviewCompleteSell = itemView.findViewById(R.id.button_reviewCompleteSell);
            button_hideOff = itemView.findViewById(R.id.button_hideOff);
            button_review = itemView.findViewById(R.id.button_review2);
            button_reviewCompleteBuy = itemView.findViewById(R.id.button_reviewCompleteBuy);
            button_renewal = itemView.findViewById(R.id.button_renewal);


            //
            imageView_goods_image = itemView.findViewById(R.id.imageView_goods_image);

            linearLayout_sale.setVisibility(View.GONE);
            button_review.setVisibility(View.GONE);
            button_hideOff.setVisibility(View.GONE);
            button_reviewCompleteBuy.setVisibility(View.GONE);

            if (button_code == 0) {
                linearLayout_sale.setVisibility(View.VISIBLE);
            } else if (button_code == 1) {
                button_reviewCompleteSell.setVisibility(View.VISIBLE);
            } else if (button_code == 2) {
                button_hideOff.setVisibility(View.VISIBLE);
            } else if (button_code == 3) {
                button_review.setVisibility(View.VISIBLE);
            }
            // 예약 속성 추가해야함
            // 예약중으로 변경에 대한 버튼 클릭 이벤트 처리
//            button_reservation.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (imageView_reservation.getVisibility() == View.GONE) {
//                        imageView_reservation.setVisibility(View.VISIBLE);
//                    } else if (imageView_reservation.getVisibility() == View.VISIBLE) {
//                        imageView_reservation.setVisibility(View.GONE);
//                    }
//                }
//            });
            // 거래완료에 대한 버튼 클릭 이벤트 처리
            button_sellComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    Log.e("[REVIEW]", "position = " + position);
                    Log.e("[REVIEW]", "거래완료 화면으로 이동");
                    Log.d("[REVIEW]", "user_name = " + user_name);
                    Intent intent = new Intent(activity, SellCompleteActivity.class);
                    intent.putExtra("item", items.get(position));
                    intent.putExtra("num", items.get(position).getNum());
                    intent.putExtra("reviewer", 0);
                    intent.putExtra("return_code", 1);
                    notifyDataSetChanged();
                    activity.startActivity(intent);
                }
            });
            // 후기작성에 대한 버튼 클릭 이벤트 처리
            button_reviewCompleteSell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    review = items.get(position);
                    Log.d("[REVIEW]", "position = " + position);
                    Intent intent = new Intent(activity, ReviewWriteActivity.class);
                    intent.putExtra("review", review);
                    intent.putExtra("user_name", user_name);
                    intent.putExtra("return_code", 1);
                    notifyDataSetChanged();
                    activity.startActivity(intent);
                }
            });
            // 숨기기해제에 대한 버튼 클릭 이벤트 처리
            button_hideOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    num = items.get(position).getNum();
                    Toast.makeText(activity, "num = " + num, Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("게시물이 다시 목록에서 노출됩니다.");
                    builder.setPositiveButton("숨기기 해제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RequestParams params = new RequestParams();
                            params.put("num", num);
                            client.post(Constants.SellBuyAdapterURL1, params, hideOffResponse);
                            // 숨김해제 Fragment 갱신
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    notifyDataSetChanged();
                }
            });
            // 후기작성 대기에 대한 버튼 클릭 이벤트 처리
            button_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    review = items.get(position);
                    Log.d("[REVIEW]", "position = " + position);
                    Intent intent = new Intent(activity, ReviewWriteActivity.class);
                    intent.putExtra("review", review);
                    intent.putExtra("user_name", user_name);
                    intent.putExtra("reviewer", 1);
                    intent.putExtra("return_code", 2);
                    activity.startActivity(intent);
                }
            });
            // 후기작성 완료에 대한 버튼 클릭 이벤트 처리
//            button_reviewCompleteBuy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });

            // ResultActivity로 이동
            imageView_goods_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    result = items.get(position);
                    Intent intent = new Intent(activity, ResultActivity.class);
                    intent.putExtra("item", result);
                    notifyDataSetChanged();
                    activity.startActivity(intent);
                }
            });


            imageLoader = ImageLoader.getInstance();
            Log.d("[INFO]", "ViewHolder 1");

//            button_context_menu.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    activity.registerForContextMenu();
//                }
//            });



            button_context_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                            MenuItem renewal, modify, hide, delete, sale, review2, not_trade;
                            if (button_code == 0) {
                                renewal = menu.add(Menu.NONE, R.id.button_sale, 1, "끌어올리기");
                                modify = menu.add(Menu.NONE, R.id.button_modify, 2, "수정");
                                hide = menu.add(Menu.NONE, R.id.button_hide, 3, "숨기기");
                                delete = menu.add(Menu.NONE, R.id.button_delete, 4, "게시물 삭제");
                                renewal.setOnMenuItemClickListener(onMenuItemClickListener);
                                modify.setOnMenuItemClickListener(onMenuItemClickListener);
                                hide.setOnMenuItemClickListener(onMenuItemClickListener);
                                delete.setOnMenuItemClickListener(onMenuItemClickListener);
                            } else if (button_code == 1) {
                                sale = menu.add(Menu.NONE, R.id.button_sale, 1, "판매중으로 변경");
                                modify = menu.add(Menu.NONE, R.id.button_modify, 2, "수정");
                                hide = menu.add(Menu.NONE, R.id.button_hide, 3, "숨기기");
                                delete = menu.add(Menu.NONE, R.id.button_delete, 4, "게시물 삭제");
                                sale.setOnMenuItemClickListener(onMenuItemClickListener);
                                modify.setOnMenuItemClickListener(onMenuItemClickListener);
                                hide.setOnMenuItemClickListener(onMenuItemClickListener);
                                delete.setOnMenuItemClickListener(onMenuItemClickListener);
                            } else if (button_code == 2) {
                                // 거래 완료 상태에 따라 끌어올리기가 있거나 없음 나중에 꼭 추가
                                review2 = menu.add(Menu.NONE, R.id.button_review2, 1, "거래 후기 남기기");
                                renewal = menu.add(Menu.NONE, R.id.button_sale, 2, "끌어올리기");
                                modify = menu.add(Menu.NONE, R.id.button_modify, 3, "수정");
                                delete = menu.add(Menu.NONE, R.id.button_delete, 4, "게시물 삭제");
                                review2.setOnMenuItemClickListener(onMenuItemClickListener);
                                renewal.setOnMenuItemClickListener(onMenuItemClickListener);
                                modify.setOnMenuItemClickListener(onMenuItemClickListener);
                                delete.setOnMenuItemClickListener(onMenuItemClickListener);
                            } else if (button_code == 3) {
                                not_trade = menu.add(Menu.NONE, R.id.button_not_trade, 1, "이런 거래한 적 없어요");
                                review2 = menu.add(Menu.NONE, R.id.button_review2, 2, "거래 후기 남기기");
                            }
                        }
                    });
                }
            });


        }

        // 컨텍스트 메뉴 처리
//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            MenuItem renewal, modify, hide, delete, sale, review2, not_trade;
//            if (button_code == 0) {
//                renewal = menu.add(Menu.NONE, R.id.button_renewal, 1, "끌어올리기");
//                modify = menu.add(Menu.NONE, R.id.button_modify, 2, "수정");
//                hide = menu.add(Menu.NONE, R.id.button_hide, 3, "숨기기");
//                delete = menu.add(Menu.NONE, R.id.button_delete, 4, "게시물 삭제");
//                renewal.setOnMenuItemClickListener(onMenuItemClickListener);
//                modify.setOnMenuItemClickListener(onMenuItemClickListener);
//                hide.setOnMenuItemClickListener(onMenuItemClickListener);
//                delete.setOnMenuItemClickListener(onMenuItemClickListener);
//            } else if (button_code == 1) {
//                sale = menu.add(Menu.NONE, R.id.button_sale, 1, "판매중으로 변경");
//                modify = menu.add(Menu.NONE, R.id.button_modify, 2, "수정");
//                hide = menu.add(Menu.NONE, R.id.button_hide, 3, "숨기기");
//                delete = menu.add(Menu.NONE, R.id.button_delete, 4, "게시물 삭제");
//                sale.setOnMenuItemClickListener(onMenuItemClickListener);
//                modify.setOnMenuItemClickListener(onMenuItemClickListener);
//                hide.setOnMenuItemClickListener(onMenuItemClickListener);
//                delete.setOnMenuItemClickListener(onMenuItemClickListener);
//            } else if (button_code == 2) {
//                // 거래 완료 상태에 따라 끌어올리기가 있거나 없음 나중에 꼭 추가
//                review2 = menu.add(Menu.NONE, R.id.button_review2, 1, "거래 후기 남기기");
//                renewal = menu.add(Menu.NONE, R.id.button_renewal, 2, "끌어올리기");
//                modify = menu.add(Menu.NONE, R.id.button_modify, 3, "수정");
//                delete = menu.add(Menu.NONE, R.id.button_delete, 4, "게시물 삭제");
//                review2.setOnMenuItemClickListener(onMenuItemClickListener);
//                renewal.setOnMenuItemClickListener(onMenuItemClickListener);
//                modify.setOnMenuItemClickListener(onMenuItemClickListener);
//                delete.setOnMenuItemClickListener(onMenuItemClickListener);
//            } else if (button_code == 3) {
//                not_trade = menu.add(Menu.NONE, R.id.button_not_trade, 1, "이런 거래한 적 없어요");
//                review2 = menu.add(Menu.NONE, R.id.button_review2, 2, "거래 후기 남기기");
//            }
//
//        }

        private final MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                        // 끌어올리기
                    case R.id.button_renewal:
                        position = getAdapterPosition();
                        modify = items.get(position);
                        RequestParams updateDateparams = new RequestParams();
                        updateDateparams.put("num", modify.getNum());
                        Log.d("[끌어올리기]", "num = " + num);
                        client.post(Constants.SellBuyAdapterUpdateDateURL, updateDateparams, hideOffResponse);
                        notifyDataSetChanged();
                        return true;
                        // 수정하기
                    case R.id.button_modify:
                        position = getAdapterPosition();
                        modify = items.get(position);
                        Intent intent_modify = new Intent(activity, ModifyActivity.class);
                        intent_modify.putExtra("item", modify);
                        notifyDataSetChanged();
                        activity.startActivity(intent_modify);
                        return true;
                        // 숨기기
                    case R.id.button_hide:
                        position = getAdapterPosition();
                        num = items.get(position).getNum();
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setMessage("다른 사용자에게 보이지 않도록 게시글을 숨기시겠어요?");
                        builder.setPositiveButton("숨기기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RequestParams params = new RequestParams();
                                params.put("num", num);
                                client.post(Constants.SellBuyAdapterURL_hide, params, hideOffResponse);
                                // 숨김해제 Fragment 갱신
                            }
                        });
                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        notifyDataSetChanged();
                        return true;
                        // 게시물 삭제
                    case R.id.button_delete:
                        position = getAdapterPosition();
                        num = items.get(position).getNum();
                        RequestParams params_delete = new RequestParams();
                        params_delete.put("num", num);
                        client.post(Constants.SellBuyAdapterURL_delete, params_delete, hideOffResponse);
                        // 숨김해제 Fragment 갱신
                        notifyDataSetChanged();
                        return true;
                        // '판매중'으로 변경
                    case R.id.button_sale:
                        position = getAdapterPosition();
                        num = items.get(position).getNum();
                        RequestParams params_sale = new RequestParams();
                        params_sale.put("num", num);
                        client.post(Constants.SellBuyAdapterURL_sale, params_sale, hideOffResponse);
                        notifyDataSetChanged();
                        return true;
                        // 거래 후기 남기기
                    case R.id.button_review2:
                        position = getAdapterPosition();
                        review = items.get(position);
                        Intent intent_review = new Intent(activity, SellCompleteActivity.class);
                        intent_review.putExtra("item", review);
                        notifyDataSetChanged();
                        activity.startActivity(intent_review);
                        return true;
                }
                return false;
            }
        };
    }


    public SellBuyAdapter(ArrayList<Main> items, int button_code, Activity activity, String user_name) {
        this.items = items;
        this.button_code = button_code;
        this.activity = activity;
        this.user_name = user_name;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Log.d("[INFO]", "리사이클러 뷰 생성");
//        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sell, parent, false);
        View item = LayoutInflater.from(activity).inflate(R.layout.list_sell, parent, false);
        ViewHolder1 vh1 = new ViewHolder1(item);
        return vh1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Log.d("[INFO]", "리사이클러 뷰 값 등록");
        Log.d("[INFO]", "items size : " + items.size());
        Main item = items.get(i);
            ViewHolder1 holder1 = (ViewHolder1) viewHolder;
            Log.d("[INFO3]", "image0 = " + item.getImage0());
            holder1.imageLoader.displayImage(item.getImage0(), holder1.imageView_image0);
            Log.d("[REVIEW]", "item.getImage0() = " + item.getImage0());
            holder1.textView_subject.setText(item.getBoard_subject());
            holder1.textView_area.setText(item.getArea());
            holder1.textview_board_date.setText(item.getBoard_date());
            holder1.textView_price.setText(item.getGoods_price() + "원");
            holder1.textView_reply_count.setText(" " + item.getReply_count());
            holder1.textView_interest_count.setText(" " + item.getInterest_count());
    }

    @Override
    public int getItemViewType(int position) {
        this.position = position;
        return position;
    }

    @Override
    public int getItemCount() {
        Log.d("[INFO]", "items size : " + items.size());
        return items.size();
    }

    public void add(Main item) {
        items.add(item);
    }

    public void clear() {
        items.clear();
    }

}
